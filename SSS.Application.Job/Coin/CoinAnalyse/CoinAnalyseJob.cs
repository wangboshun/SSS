using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

using Polly;

using Quartz;

using SSS.Application.Job.JobSetting.Extension;
using SSS.DigitalCurrency.Domain;
using SSS.DigitalCurrency.Huobi;
using SSS.DigitalCurrency.Indicator;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Net;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Application.Job.Coin.CoinAnalyse
{
    [DIService(ServiceLifetime.Transient, typeof(CoinAnalyseJob))]
    public class CoinAnalyseJob : IJob
    {
        private readonly HuobiUtils _huobi;

        private readonly Indicator _indicator;
        private readonly ILogger _logger;
        private readonly IServiceScopeFactory _scopeFactory;
        private static object _lock = new object();

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
                Stopwatch watch = new Stopwatch();
                watch.Start();

                var trigger = (Quartz.Impl.Triggers.CronTriggerImpl)((Quartz.Impl.JobExecutionContextImpl)context).Trigger;
                try
                {
                    var coin_array = _huobi.GetAllCoin();

                    Parallel.ForEach(coin_array, (item) =>
                    {
                        List<Domain.Coin.CoinKLineData.CoinKLineData> kline = null;

                        var retry = Policy.Handle<WebException>().Retry(3, (ex, count, text) =>
                        {
                            _logger.LogError(new EventId(ex.HResult), ex, $"---CoinAnalyseJob GetKLine Exception,进行重试 {count}次---");
                            Thread.Sleep(100);
                        });

                        retry.Execute(() =>
                        {
                            kline = _huobi.GetKLine(item.base_currency, item.quote_currency, CoinTime.Time_1day.ToString().Split('_')[1], 2000);
                        });

                        if (kline == null)
                            return;

                        string coin = item.base_currency + "-" + item.quote_currency;

                        Average(coin, kline, CoinTime.Time_1day);
                        MACD(coin, kline, CoinTime.Time_1day);
                        KDJ(coin, kline, CoinTime.Time_1day);
                    });

                    Analyse(CoinTime.Time_1day);

                    watch.Stop();
                    context.Scheduler.Context.Put(trigger.FullName + "_Result", this.GetType());
                    context.Scheduler.Context.Put(trigger.FullName + "_Time", watch.ElapsedMilliseconds);

                    _logger.LogInformation($"------>{context.GetJobDetail()}  耗时：{watch.ElapsedMilliseconds} ");
                    return Task.FromResult($"{trigger.FullName} Success");
                }
                catch (Exception ex)
                {
                    context.Scheduler.Context.Put(trigger.FullName + "_Exception", ex);
                    return Task.FromResult(trigger.FullName + "Exception");
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
                    using var context = scope.ServiceProvider.GetRequiredService<CoinDbContext>();

                    var Average = context.CoinAnalyse.Where(x => x.IsDelete == 0 && x.IndicatorType == 1).ToList();
                    var Macd = context.CoinAnalyse.Where(x => x.IsDelete == 0 && x.IndicatorType == 2).ToList();
                    var Kdj = context.CoinAnalyse.Where(x => x.IsDelete == 0 && x.IndicatorType == 3).ToList();
                    var Fast = context.CoinAnalyse.Where(x => x.IsDelete == 0 && x.IndicatorType == 4).ToList();

                    List<Domain.Coin.CoinAnalyse.CoinAnalyse> list = new List<Domain.Coin.CoinAnalyse.CoinAnalyse>();
                    list.AddRange(Average);

                    TotalDesc(list, Macd);
                    TotalDesc(list, Kdj);
                    TotalDesc(list, Fast);

                    List<string> removecoin = new List<string>();
                    list = list.GroupBy(c => c.Coin).Select(c => c.First()).ToList();
                    List<Domain.Coin.CoinAnalyse.CoinAnalyse> list_coin = new List<Domain.Coin.CoinAnalyse.CoinAnalyse>();
                    foreach (var item in list)
                        if (item.Desc.Contains("☆"))
                        {
                            removecoin.Add(item.Coin);
                            Domain.Coin.CoinAnalyse.CoinAnalyse model = new Domain.Coin.CoinAnalyse.CoinAnalyse
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
                            list_coin.Add(model);
                        }

                    if (!list_coin.Any()) return;
                    context.Database.ExecuteSqlRaw("UPDATE CoinAnalyse SET IsDelete=1 where IndicatorType=0 ");
                    string sql = @"UPDATE CoinAnalyse SET IsDelete=1 where Coin in ('{0}')";
                    sql = string.Format(sql, string.Join("','", removecoin.ToArray()));
                    context.Database.ExecuteSqlRaw(sql);
                    context.CoinAnalyse.AddRange(list_coin);
                    context.SaveChanges();
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
                Console.WriteLine($"---{ coin} Average---");

                if (kline == null)
                    return;

                var data5 = _indicator.SMA(kline, 5);
                var data10 = _indicator.SMA(kline, 10);
                var data30 = _indicator.SMA(kline, 30);
                var data60 = _indicator.SMA(kline, 60);

                //获取时间段
                string typename = GetTimeType(type);
                List<Domain.Coin.CoinAnalyse.CoinAnalyse> list_coin = new List<Domain.Coin.CoinAnalyse.CoinAnalyse>();
                if (data5.Count > 0 && data10.Count > 0 && data5.First().Item2 > data10.First().Item2)
                {
                    Domain.Coin.CoinAnalyse.CoinAnalyse model = new Domain.Coin.CoinAnalyse.CoinAnalyse
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
                if (!list_coin.Any()) return;
                using var scope = _scopeFactory.CreateScope();
                using var context = scope.ServiceProvider.GetRequiredService<CoinDbContext>();
                context.Database.ExecuteSqlRaw("UPDATE CoinAnalyse SET IsDelete=1 where IndicatorType=1 ");
                context.CoinAnalyse.AddRange(list_coin);
                context.SaveChanges();
                Console.WriteLine("---Average  SaveChanges---");
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---Average---");
            }
            return;
        }

        #endregion

        #region MACD

        public void MACD(string coin, List<Domain.Coin.CoinKLineData.CoinKLineData> kline, CoinTime type)
        {
            try
            {
                Console.WriteLine($"---{ coin} MACD---");

                if (kline == null || kline.Count < 1)
                    return;

                var macd = _indicator.MACD(kline);

                if (macd.Count < 1 || macd.FirstOrDefault()?.Item2 < macd.FirstOrDefault()?.Item3)
                    return;

                //获取时间段
                string typename = GetTimeType(type);
                List<Domain.Coin.CoinAnalyse.CoinAnalyse> list_coin = new List<Domain.Coin.CoinAnalyse.CoinAnalyse>();
                Domain.Coin.CoinAnalyse.CoinAnalyse model = new Domain.Coin.CoinAnalyse.CoinAnalyse
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

                if (!list_coin.Any()) return;
                using var scope = _scopeFactory.CreateScope();
                using var context = scope.ServiceProvider.GetRequiredService<CoinDbContext>();
                context.Database.ExecuteSqlRaw("UPDATE CoinAnalyse SET IsDelete=1 where IndicatorType=2 ");
                context.CoinAnalyse.AddRange(list_coin);
                context.SaveChanges();
                Console.WriteLine("---MACD  SaveChanges---");
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
                Console.WriteLine($"---{ coin} KDJ---");

                if (kline == null || kline.Count < 1)
                    return;

                var kdj = _indicator.KDJ(kline);
                //获取时间段
                string typename = GetTimeType(type);

                string desc = $"【{typename}级别,KDJ金叉】";

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
                List<Domain.Coin.CoinAnalyse.CoinAnalyse> list_coin = new List<Domain.Coin.CoinAnalyse.CoinAnalyse>();
                Domain.Coin.CoinAnalyse.CoinAnalyse model = new Domain.Coin.CoinAnalyse.CoinAnalyse
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

                if (!list_coin.Any()) return;
                using var scope = _scopeFactory.CreateScope();
                using var context = scope.ServiceProvider.GetRequiredService<CoinDbContext>();
                context.Database.ExecuteSqlRaw("UPDATE CoinAnalyse SET IsDelete=1 where IndicatorType=3 ");
                context.CoinAnalyse.AddRange(list_coin);
                context.SaveChanges();
                Console.WriteLine("---KDJ  SaveChanges---");
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
            string logo = "https://s1.bqiapp.com/coin/20181030_72_png/bitcoin_200_200.png?v=1566978037";
            try
            {
                using var scope = _scopeFactory.CreateScope();
                using var context = scope.ServiceProvider.GetRequiredService<CoinDbContext>();
                var info = context.CoinInfo.FirstOrDefault(x => x.Coin.Equals(coin.ToUpper()));
                if (info != null)
                    return info.RomteLogo;
                return logo;
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