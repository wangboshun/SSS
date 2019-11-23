using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

using SSS.DigitalCurrency.Domain;
using SSS.DigitalCurrency.Huobi;
using SSS.DigitalCurrency.Indicator;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Config;

using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;



namespace SSS.Application.DigitalCurrency.Job
{
    [DIService(ServiceLifetime.Transient, typeof(IHostedService))]
    public class DigitalCurrencyJob : IHostedService, IDisposable
    {
        private readonly ILogger _logger;
        private readonly IServiceScopeFactory _scopeFactory;

        private readonly Indicator _indicator;
        private readonly HuobiUtils _huobi;

        private readonly List<Domain.DigitalCurrency.DigitalCurrency> ListCoin =
            new List<Domain.DigitalCurrency.DigitalCurrency>();

        private readonly List<Domain.DigitalCurrency.DigitalCurrency> FastListCoin =
          new List<Domain.DigitalCurrency.DigitalCurrency>();

        private static int FastFlag = 0;
        private Timer _timer1;
        private Timer _timer2;

        public DigitalCurrencyJob(ILogger<DigitalCurrencyJob> logger, IServiceScopeFactory scopeFactory, HuobiUtils huobi, Indicator indicator)
        {
            _logger = logger;
            _scopeFactory = scopeFactory;
            _huobi = huobi;
            _indicator = indicator;
        }

        public void Dispose()
        {
            _timer1?.Dispose();
            _timer2?.Dispose();
        }

        public Task StartAsync(CancellationToken stoppingToken)
        {
            _timer1 = new Timer(DoWorkForFast, null, TimeSpan.Zero,
              TimeSpan.FromMinutes(2));

            _timer2 = new Timer(DoWork, null, TimeSpan.Zero,
                TimeSpan.FromMinutes(30));

            return Task.CompletedTask;
        }

        public Task StopAsync(CancellationToken stoppingToken)
        {
            _timer1?.Change(Timeout.Infinite, 0);
            _timer2?.Change(Timeout.Infinite, 0);

            return Task.CompletedTask;
        }

        private void DoWorkForFast(object state)
        {
            if (Config.GetSectionValue("JobManager:DigitalCurrencyJob").Equals("OFF"))
                return;

            _logger.LogInformation("---爆拉分析---");
            if (FastFlag == 1)
                Fast(CoinTime.Time_5min);
        }

        private void DoWork(object state)
        {
            if (Config.GetSectionValue("JobManager:DigitalCurrencyJob").Equals("OFF"))
                return;

            FastFlag = 0;
            Average(CoinTime.Time_1day);
            MACD(CoinTime.Time_1day);
            KDJ(CoinTime.Time_1day);
            Analyse(CoinTime.Time_1day);
            FastFlag = 1;
        }

        /// <summary>
        /// 爆拉分析
        /// </summary>
        public void Fast(CoinTime type)
        {
            try
            {
                using var scope = _scopeFactory.CreateScope();
                using var context = scope.ServiceProvider.GetRequiredService<DbcontextBase>();

                List<CoinSymbols> allcoin = _huobi.GetAllCoin();

                foreach (var coin in allcoin)
                {
                    var kline = _huobi.GetKLine(coin.base_currency, coin.quote_currency, type.ToString().Split('_')[1], 6);

                    if (kline == null || kline.Count < 1)
                        continue;

                    if (kline.First().close / kline.Last().close > 1.1)
                    {
                        Domain.DigitalCurrency.DigitalCurrency model = new Domain.DigitalCurrency.DigitalCurrency
                        {
                            Id = Guid.NewGuid().ToString(),
                            Coin = coin.base_currency.ToUpper() + "-" + coin.quote_currency.ToUpper(),
                            CreateTime = DateTime.Now,
                            Platform = "火币",
                            CloseRange = 0.1,
                            HighRange = 0.1,
                            Open = 100,
                            Close = 110,
                            TimeType = GetTimeType(type),
                            IsDelete = 0,
                            Desc = "【" + "短时间内，爆拉" + "】",
                            IndicatorType = 4
                        };
                        FastListCoin.Add(model);
                    }
                }

                if (FastListCoin.Any())
                {
                    context.Database.ExecuteSqlRaw("UPDATE DigitalCurrency SET IsDelete=1 where IndicatorType=4 ");
                    context.DigitalCurrency.AddRange(FastListCoin);
                    context.SaveChanges();
                    FastListCoin.Clear();
                    Console.WriteLine("---Fast  SaveChanges---");
                }
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---Fast---");
            }
        }

        /// <summary>
        /// 综合分析
        /// </summary>
        /// <param name="type"></param>
        public void Analyse(CoinTime type)
        {
            try
            {
                using var scope = _scopeFactory.CreateScope();
                using var context = scope.ServiceProvider.GetRequiredService<DbcontextBase>();

                var Average = context.DigitalCurrency.Where(x => x.IsDelete == 0 && x.IndicatorType == 1).ToList();
                var Macd = context.DigitalCurrency.Where(x => x.IsDelete == 0 && x.IndicatorType == 2).ToList();
                var Kdj = context.DigitalCurrency.Where(x => x.IsDelete == 0 && x.IndicatorType == 3).ToList();
                var Fast = context.DigitalCurrency.Where(x => x.IsDelete == 0 && x.IndicatorType == 4).ToList();

                List<SSS.Domain.DigitalCurrency.DigitalCurrency> list = new List<SSS.Domain.DigitalCurrency.DigitalCurrency>();
                list.AddRange(Average);

                TotalDesc(list, Macd);
                TotalDesc(list, Kdj);
                TotalDesc(list, Fast);

                List<string> removecoin = new List<string>();
                list = list.GroupBy(c => c.Coin).Select(c => c.First()).ToList();

                foreach (var item in list)
                {
                    if (item.Desc.Contains("☆"))
                    {
                        removecoin.Add(item.Coin);
                        Domain.DigitalCurrency.DigitalCurrency model = new Domain.DigitalCurrency.DigitalCurrency
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
                        ListCoin.Add(model);
                    }
                }

                if (ListCoin.Any())
                {
                    context.Database.ExecuteSqlRaw("UPDATE DigitalCurrency SET IsDelete=1 where IndicatorType=0 ");
                    string sql = @"UPDATE DigitalCurrency SET IsDelete=1 where Coin in ('{0}')";
                    sql = string.Format(sql, string.Join("','", removecoin.ToArray()));
                    context.Database.ExecuteSqlRaw(sql);
                    context.DigitalCurrency.AddRange(ListCoin);
                    context.SaveChanges();
                    ListCoin.Clear();
                    Console.WriteLine("---Analyse  SaveChanges---");
                }
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---Analyse---");
            }
        }

        private void TotalDesc(List<SSS.Domain.DigitalCurrency.DigitalCurrency> list, List<SSS.Domain.DigitalCurrency.DigitalCurrency> source)
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
        public void Average(CoinTime type)
        {
            try
            {
                Console.WriteLine("---Average---");

                List<CoinSymbols> allcoin = _huobi.GetAllCoin();

                foreach (var coin in allcoin)
                {
                    var kline = _huobi.GetKLine(coin.base_currency, coin.quote_currency, type.ToString().Split('_')[1], 2000);

                    if (kline == null || kline.Count < 1)
                        continue;

                    var data5 = _indicator.SMA(kline, 5);
                    var data10 = _indicator.SMA(kline, 10);
                    var data30 = _indicator.SMA(kline, 30);
                    var data60 = _indicator.SMA(kline, 60);

                    //获取时间段
                    string typename = GetTimeType(type);

                    if (data5.Count > 0 && data10.Count > 0 && data5.First().Item2 > data10.First().Item2)
                    {
                        Domain.DigitalCurrency.DigitalCurrency model = new Domain.DigitalCurrency.DigitalCurrency
                        {
                            Id = Guid.NewGuid().ToString(),
                            Coin = coin.base_currency.ToUpper() + "-" + coin.quote_currency.ToUpper(),
                            TimeType = typename,
                            CreateTime = DateTime.Now,
                            Platform = "火币",
                            IsDelete = 0,
                            Close = kline.First().close,
                            Open = kline.First().open,
                            High = kline.First().high,
                            Low = kline.First().low,
                            IndicatorType = 1
                        };

                        if (data5.Count > 0 && data30.Count > 0 && data5.First().Item2 > data30.First().Item2)
                        {
                            if (data5.Count > 0 && data60.Count > 0 && data5.First().Item2 > data60.First().Item2)
                            {
                                Console.WriteLine(coin.base_currency.ToUpper() + "—" + coin.quote_currency.ToUpper() + $"【{typename}】突破60K压力位,金叉");
                                model.Desc = $"【{typename}级别,均线突破60K压力位】";
                            }
                            else
                            {
                                Console.WriteLine(coin.base_currency.ToUpper() + "—" + coin.quote_currency.ToUpper() + $"【{typename}】突破30K压力位,金叉");
                                model.Desc = $"【{typename}级别,均线突破30K压力位】";
                            }
                        }
                        else
                        {
                            model.Desc = $"【{typename}级别,均线突破10K压力位】";
                            Console.WriteLine(coin.base_currency.ToUpper() + "—" + coin.quote_currency.ToUpper() + $"【{typename}】突破10K压力位,金叉");
                        }

                        model.HighRange = model.High / model.Low - 1;
                        model.CloseRange = model.Close / model.Open - 1;
                        ListCoin.Add(model);
                    }
                }
                using var scope = _scopeFactory.CreateScope();
                using var context = scope.ServiceProvider.GetRequiredService<DbcontextBase>();

                if (ListCoin.Any())
                {
                    context.Database.ExecuteSqlRaw("UPDATE DigitalCurrency SET IsDelete=1 where IndicatorType=1 ");
                    context.DigitalCurrency.AddRange(ListCoin);
                    context.SaveChanges();
                    ListCoin.Clear();
                    Console.WriteLine("---Average  SaveChanges---");
                }
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---Average---");
            }
        }

        #endregion

        #region MACD

        public void MACD(CoinTime type)
        {
            try
            {
                Console.WriteLine("---MACD---");

                List<CoinSymbols> allcoin = _huobi.GetAllCoin();

                foreach (var coin in allcoin)
                {
                    var kline = _huobi.GetKLine(coin.base_currency, coin.quote_currency, type.ToString().Split('_')[1], 2000);

                    if (kline == null || kline.Count < 1)
                        continue;

                    var macd = _indicator.MACD(kline);

                    if (macd.Count < 1 || macd.FirstOrDefault()?.Item2 < macd.FirstOrDefault()?.Item3)
                        continue;

                    //获取时间段
                    string typename = GetTimeType(type);

                    Domain.DigitalCurrency.DigitalCurrency model = new Domain.DigitalCurrency.DigitalCurrency
                    {
                        Id = Guid.NewGuid().ToString(),
                        Coin = coin.base_currency.ToUpper() + "-" + coin.quote_currency.ToUpper(),
                        TimeType = typename,
                        CreateTime = DateTime.Now,
                        Platform = "火币",
                        IsDelete = 0,
                        Close = kline.First().close,
                        Open = kline.First().open,
                        High = kline.First().high,
                        Low = kline.First().low,
                        IndicatorType = 2,
                        Desc = $"【{typename}级别,MACD金叉】"
                    };

                    model.HighRange = model.High / model.Low - 1;
                    model.CloseRange = model.Close / model.Open - 1;

                    Console.WriteLine(coin.base_currency.ToUpper() + "—" + coin.quote_currency.ToUpper() + $"【{typename}】MACD,金叉");

                    ListCoin.Add(model);
                }

                using var scope = _scopeFactory.CreateScope();
                using var context = scope.ServiceProvider.GetRequiredService<DbcontextBase>();

                if (ListCoin.Any())
                {
                    context.Database.ExecuteSqlRaw("UPDATE DigitalCurrency SET IsDelete=1 where IndicatorType=2 ");
                    context.DigitalCurrency.AddRange(ListCoin);
                    context.SaveChanges();
                    ListCoin.Clear();
                    Console.WriteLine("---MACD  SaveChanges---");
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        #endregion

        #region KDJ

        public void KDJ(CoinTime type)
        {
            try
            {
                Console.WriteLine("---KDJ---");

                List<CoinSymbols> allcoin = _huobi.GetAllCoin();

                foreach (var coin in allcoin)
                {
                    var kline = _huobi.GetKLine(coin.base_currency, coin.quote_currency, type.ToString().Split('_')[1], 2000);

                    if (kline == null || kline.Count < 1)
                        continue;

                    var kdj = _indicator.KDJ(kline);
                    //获取时间段
                    string typename = GetTimeType(type);

                    string desc = $"【{typename}级别,KDJ金叉】";

                    //J值大于K值
                    if (kdj.Count < 1 || kdj.FirstOrDefault()?.Item4 < kdj.FirstOrDefault()?.Item2)
                    {
                        //超卖状态
                        if (kdj.FirstOrDefault()?.Item2 < 20 && kdj.FirstOrDefault()?.Item3 < 20 && kdj.FirstOrDefault()?.Item4 < 20)
                            desc = $"【{typename}级别,KDJ超卖状态，建议买入】";
                        else
                            continue;
                    }

                    //超买状态
                    if (kdj.FirstOrDefault()?.Item2 > 80 && kdj.FirstOrDefault()?.Item3 > 80 && kdj.FirstOrDefault()?.Item4 > 80)
                        desc = $"【{typename}级别,KDJ超买状态，建议卖出】";

                    Domain.DigitalCurrency.DigitalCurrency model = new Domain.DigitalCurrency.DigitalCurrency
                    {
                        Id = Guid.NewGuid().ToString(),
                        Coin = coin.base_currency.ToUpper() + "-" + coin.quote_currency.ToUpper(),
                        TimeType = typename,
                        CreateTime = DateTime.Now,
                        Platform = "火币",
                        IsDelete = 0,
                        Close = kline.First().close,
                        Open = kline.First().open,
                        High = kline.First().high,
                        Low = kline.First().low,
                        IndicatorType = 3,
                        Desc = desc
                    };

                    model.HighRange = model.High / model.Low - 1;
                    model.CloseRange = model.Close / model.Open - 1;

                    Console.WriteLine(coin.base_currency.ToUpper() + "—" + coin.quote_currency.ToUpper() + desc);

                    ListCoin.Add(model);
                }

                using var scope = _scopeFactory.CreateScope();
                using var context = scope.ServiceProvider.GetRequiredService<DbcontextBase>();

                if (ListCoin.Any())
                {
                    context.Database.ExecuteSqlRaw("UPDATE DigitalCurrency SET IsDelete=1 where IndicatorType=3 ");
                    context.DigitalCurrency.AddRange(ListCoin);
                    context.SaveChanges();
                    ListCoin.Clear();
                    Console.WriteLine("---KDJ  SaveChanges---");
                }
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
                using var context = scope.ServiceProvider.GetRequiredService<DbcontextBase>();
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

                case CoinTime.Time4_60min:
                    return "4小时";

                case CoinTime.Time_1day:
                    return "日线";
            }

            return "";
        }
        #endregion
    }
}