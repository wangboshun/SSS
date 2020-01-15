using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

using Polly;

using Quartz;
using Quartz.Impl;
using Quartz.Impl.Triggers;

using SSS.Application.Job.JobSetting.Extension;
using SSS.DigitalCurrency.Domain;
using SSS.DigitalCurrency.Huobi;
using SSS.DigitalCurrency.Indicator;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Net;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Application.Job.Coin.CoinAnalyse
{
    [DIService(ServiceLifetime.Singleton, typeof(CoinAnalyseJob))]
    public class CoinAnalyseJob : IJob
    {
        private readonly ILogger _logger;
        private readonly HuobiUtils _huobi;
        private readonly Indicator _indicator;
        private readonly IServiceScopeFactory _scopeFactory;
        private readonly ConcurrentBag<Domain.Coin.CoinAnalyse.CoinAnalyse> list_coin = new ConcurrentBag<Domain.Coin.CoinAnalyse.CoinAnalyse>();
        private static readonly object _lock = new object();

        public CoinAnalyseJob(ILogger<CoinAnalyseJob> logger, IServiceScopeFactory scopeFactory, HuobiUtils huobi, Indicator indicator)
        {
            _logger = logger;
            _scopeFactory = scopeFactory;
            _huobi = huobi;
            _indicator = indicator;
        }

        public Task Execute(IJobExecutionContext context)
        {
            _logger.LogInformation("-----------------CoinAnalyseJob----------------------");
            return DoWork(context);
        }

        public Task DoWork(IJobExecutionContext context)
        {
            lock (_lock)
            {
                var trigger = (CronTriggerImpl)((JobExecutionContextImpl)context).Trigger;
                try
                {
                    var watch = new Stopwatch();
                    watch.Start();
                    var coin_array = _huobi.GetAllCoin();

                    Parallel.ForEach(coin_array, item =>
                    {
                        List<Domain.Coin.CoinKLineData.CoinKLineData> kline = null;

                        var retry = Policy.Handle<WebException>().Retry(3, (ex, count, text) =>
                        {
                            _logger.LogError(new EventId(ex.HResult), ex,$"---CoinAnalyseJob GetKLine Exception,进行重试 {count}次---");
                            Thread.Sleep(100);
                        });

                        retry.Execute(() =>
                        {
                            kline = _huobi.GetKLine(item.base_currency, item.quote_currency,CoinTime.Time_1day.ToString().Split('_')[1], 2000);
                        });

                        if (kline == null)
                            return;

                        var coin = item.base_currency + "-" + item.quote_currency;

                        Average(coin, kline, CoinTime.Time_1day);
                        MACD(coin, kline, CoinTime.Time_1day);
                        KDJ(coin, kline, CoinTime.Time_1day);
                    });

                    if (list_coin.Any())
                    {
                        using var scope = _scopeFactory.CreateScope();
                        using var db_context = scope.ServiceProvider.GetRequiredService<CoinDbContext>();
                        db_context.Database.ExecuteSqlRaw("UPDATE CoinAnalyse SET IsDelete=1 where IndicatorType<>0 ");
                        db_context.CoinAnalyse.AddRange(list_coin);
                        db_context.SaveChanges();
                    }

                    Analyse(CoinTime.Time_1day);

                    watch.Stop();
                    context.Scheduler.Context.Put(trigger.FullName + "_Result", "Success");
                    context.Scheduler.Context.Put(trigger.FullName + "_Time", watch.ElapsedMilliseconds);

                    _logger.LogInformation($"------>{context.GetJobDetail()}  耗时：{watch.ElapsedMilliseconds} ");
                    return Task.FromResult("Success");
                }
                catch (Exception ex)
                {
                    context.Scheduler.Context.Put(trigger.FullName + "_Exception", ex);
                    _logger.LogError(new EventId(ex.HResult), ex, "---CoinAnalyseJob DoWork Exception---");
                    return Task.FromResult("Error");
                }
            }
        }

        /// <summary>
        ///     综合分析
        /// </summary>
        /// <param name="type"></param>
        public void Analyse(CoinTime type)
        {
            lock (_lock)
            {
                try
                {
                    using var scope = _scopeFactory.CreateScope();
                    using var db_context = scope.ServiceProvider.GetRequiredService<CoinDbContext>();

                    var Average = db_context.CoinAnalyse.Where(x => x.IsDelete == 0 && x.IndicatorType == 1).ToList();
                    var Macd = db_context.CoinAnalyse.Where(x => x.IsDelete == 0 && x.IndicatorType == 2).ToList();
                    var Kdj = db_context.CoinAnalyse.Where(x => x.IsDelete == 0 && x.IndicatorType == 3).ToList();
                    var Fast = db_context.CoinAnalyse.Where(x => x.IsDelete == 0 && x.IndicatorType == 4).ToList();

                    var list = new List<Domain.Coin.CoinAnalyse.CoinAnalyse>();
                    list.AddRange(Average);
                    list.AddRange(Macd);
                    list.AddRange(Kdj);
                    list.AddRange(Fast);

                    TotalDesc(list, Macd);
                    TotalDesc(list, Kdj);
                    TotalDesc(list, Fast);

                    var removecoin = new List<string>();
                    list = list.GroupBy(c => c.Coin).Select(c => c.First()).ToList();
                    var temp_list_coin = new List<Domain.Coin.CoinAnalyse.CoinAnalyse>();

                    foreach (var item in list.Where(item => item.Desc.Contains("☆")))
                    {
                        removecoin.Add(item.Coin);
                        var model = new Domain.Coin.CoinAnalyse.CoinAnalyse
                        {
                            Id = Guid.NewGuid().ToString(),
                            Coin = item.Coin,
                            CreateTime = DateTime.Now,

                            Platform = "火币",
                            IsDelete = 0,
                            Open = item.Open,
                            Close = item.Close,
                            CloseRange = item.CloseRange,
                            HighRange = item.HighRange,
                            TimeType = GetTimeType(type),
                            Desc = item.Desc,
                            IndicatorType = 0
                        };
                        temp_list_coin.Add(model);
                    }

                    if (!temp_list_coin.Any()) return;
                    db_context.Database.ExecuteSqlRaw("UPDATE CoinAnalyse SET IsDelete=1 where IndicatorType=0 ");
                    var sql = @"UPDATE CoinAnalyse SET IsDelete=1 where Coin in ('{0}')";
                    sql = string.Format(sql, string.Join("','", removecoin.ToArray()));
                    db_context.Database.ExecuteSqlRaw(sql);
                    db_context.CoinAnalyse.AddRange(temp_list_coin);
                    db_context.SaveChanges();
                    Console.WriteLine("---Analyse  SaveChanges---");
                }
                catch (Exception ex)
                {
                    _logger.LogError(new EventId(ex.HResult), ex, "---Analyse---");
                }
            }
        }

        private void TotalDesc(List<Domain.Coin.CoinAnalyse.CoinAnalyse> list,
            List<Domain.Coin.CoinAnalyse.CoinAnalyse> source)
        {
            foreach (var item in source)
            {
                var model = list.FirstOrDefault(x => x.Coin.Equals(item.Coin));
                if (model == null)
                    continue;

                list.Remove(model);
                model.Desc += "☆" + item.Desc;
                list.Add(model);
            }
        }

        #region 均线系统

        /// <summary>
        ///     均线系统
        /// </summary>
        public void Average(string coin, List<Domain.Coin.CoinKLineData.CoinKLineData> kline, CoinTime type)
        {
            try
            {
                Console.WriteLine($"---{coin} Average---");

                if (kline == null)
                    return;

                var data5 = _indicator.SMA(kline, 5);
                var data10 = _indicator.SMA(kline, 10);
                var data30 = _indicator.SMA(kline, 30);
                var data60 = _indicator.SMA(kline, 60);

                //获取时间段
                var typename = GetTimeType(type);

                if (data5.Count > 0 && data10.Count > 0 && data5.First().Item2 > data10.First().Item2)
                {
                    var model = new Domain.Coin.CoinAnalyse.CoinAnalyse
                    {
                        Id = Guid.NewGuid().ToString(),
                        Coin = coin,
                        TimeType = typename,
                        CreateTime = DateTime.Now,
                        Platform = "火币",
                        IsDelete = 0,
                        Close = kline.First().Close,
                        Open = kline.First().Open,
                        High = kline.First().High,
                        Low = kline.First().Low,
                        IndicatorType = 1
                    };

                    if (data5.Count > 0 && data30.Count > 0 && data5.First().Item2 > data30.First().Item2)
                    {
                        if (data5.Count > 0 && data60.Count > 0 && data5.First().Item2 > data60.First().Item2)
                        {
                            Console.WriteLine($" {coin}【{typename}】突破60K压力位,金叉");
                            model.Desc = $"【{typename}级别,均线突破60K压力位】";
                        }
                        else
                        {
                            Console.WriteLine($" {coin}【{typename}】突破30K压力位,金叉");
                            model.Desc = $"【{typename}级别,均线突破30K压力位】";
                        }
                    }
                    else
                    {
                        model.Desc = $"【{typename}级别,均线突破10K压力位】";
                        Console.WriteLine($" {coin}【{typename}】突破10K压力位,金叉");
                    }

                    model.HighRange = model.High / model.Low - 1;
                    model.CloseRange = model.Close / model.Open - 1;
                    list_coin.Add(model);
                }

                Console.WriteLine("---Average  SaveChanges---");
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---Average---");
            }
        }

        #endregion

        #region MACD

        public void MACD(string coin, List<Domain.Coin.CoinKLineData.CoinKLineData> kline, CoinTime type)
        {
            try
            {
                Console.WriteLine($"---{coin} MACD---");

                if (kline == null || kline.Count < 1)
                    return;

                var macd = _indicator.MACD(kline);

                if (macd.Count < 1 || macd.FirstOrDefault()?.Item2 < macd.FirstOrDefault()?.Item3)
                    return;

                //获取时间段
                var typename = GetTimeType(type);

                var model = new Domain.Coin.CoinAnalyse.CoinAnalyse
                {
                    Id = Guid.NewGuid().ToString(),
                    Coin = coin,
                    TimeType = typename,
                    CreateTime = DateTime.Now,
                    Platform = "火币",
                    IsDelete = 0,
                    Close = kline.First().Close,
                    Open = kline.First().Open,
                    High = kline.First().High,
                    Low = kline.First().Low,
                    IndicatorType = 2,
                    Desc = $"【{typename}级别,MACD金叉】"
                };

                model.HighRange = model.High / model.Low - 1;
                model.CloseRange = model.Close / model.Open - 1;

                Console.WriteLine($" {coin}【{typename}】MACD,金叉");
                list_coin.Add(model);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        #endregion

        #region KDJ

        public void KDJ(string coin, List<Domain.Coin.CoinKLineData.CoinKLineData> kline, CoinTime type)
        {
            try
            {
                Console.WriteLine($"---{coin} KDJ---");

                if (kline == null || kline.Count < 1)
                    return;

                var kdj = _indicator.KDJ(kline);
                //获取时间段
                var typename = GetTimeType(type);

                var desc = $"【{typename}级别,KDJ金叉】";

                //J值大于K值
                if (kdj.Count < 1 || kdj.FirstOrDefault()?.Item4 < kdj.FirstOrDefault()?.Item2)
                {
                    //超卖状态
                    if (kdj.FirstOrDefault()?.Item2 < 20 && kdj.FirstOrDefault()?.Item3 < 20 &&
                        kdj.FirstOrDefault()?.Item4 < 20)
                        desc = $"【{typename}级别,KDJ超卖状态，建议买入】";
                    else
                        return;
                }

                //超买状态
                if (kdj.FirstOrDefault()?.Item2 > 80 && kdj.FirstOrDefault()?.Item3 > 80 &&
                    kdj.FirstOrDefault()?.Item4 > 80)
                    desc = $"【{typename}级别,KDJ超买状态，建议卖出】";

                var model = new Domain.Coin.CoinAnalyse.CoinAnalyse
                {
                    Id = Guid.NewGuid().ToString(),
                    Coin = coin,
                    TimeType = typename,
                    CreateTime = DateTime.Now,
                    Platform = "火币",
                    IsDelete = 0,
                    Close = kline.First().Close,
                    Open = kline.First().Open,
                    High = kline.First().High,
                    Low = kline.First().Low,
                    IndicatorType = 3,
                    Desc = desc
                };

                model.HighRange = model.High / model.Low - 1;
                model.CloseRange = model.Close / model.Open - 1;

                Console.WriteLine($" {coin}【{typename}】KDJ,{desc}");
                list_coin.Add(model);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        #endregion

        #region 公共

        /// <summary>
        ///     获取币币的Logo
        /// </summary>
        /// <returns></returns>
        public string GetLogo(string coin)
        {
            var logo = "https://s1.bqiapp.com/coin/20181030_72_png/bitcoin_200_200.png?v=1566978037";
            try
            {
                using var scope = _scopeFactory.CreateScope();
                using var db_context = scope.ServiceProvider.GetRequiredService<CoinDbContext>();
                var info = db_context.CoinInfo.FirstOrDefault(x => x.Coin.Equals(coin.ToUpper()));
                return info != null ? info.RomteLogo : logo;
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---GetLogo---");
                return logo;
            }
        }

        /// <summary>
        ///     获取时间段
        /// </summary>
        /// <param name="type"></param>
        /// <returns></returns>
        private string GetTimeType(CoinTime type)
        {
            switch (type)
            {
                case CoinTime.Time_1min:
                    return "1分钟";
                case CoinTime.Time_5min:
                    return "5分钟";
                case CoinTime.Time_15min:
                    return "15分钟";

                case CoinTime.Time_60min:
                    return "1小时";

                case CoinTime.Time_4hour:
                    return "4小时";

                case CoinTime.Time_1day:
                    return "日线";
            }

            return "";
        }

        #endregion
    }
}