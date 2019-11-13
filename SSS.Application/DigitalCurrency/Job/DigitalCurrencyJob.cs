using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

using SSS.DigitalCurrency.Domain;
using SSS.DigitalCurrency.Huobi;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using SSS.DigitalCurrency.Indicator;

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

        private Timer _timer;

        public DigitalCurrencyJob(ILogger<DigitalCurrencyJob> logger, IServiceScopeFactory scopeFactory, HuobiUtils huobi, Indicator indicator)
        {
            _logger = logger;
            _scopeFactory = scopeFactory;
            _huobi = huobi;
            _indicator = indicator;
        }

        public void Dispose()
        {
            _timer?.Dispose();
        }

        public Task StartAsync(CancellationToken stoppingToken)
        {
            _timer = new Timer(DoWork, null, TimeSpan.Zero,
                TimeSpan.FromMinutes(60));

            return Task.CompletedTask;
        }

        public Task StopAsync(CancellationToken stoppingToken)
        {
            _timer?.Change(Timeout.Infinite, 0);

            return Task.CompletedTask;
        }

        private void DoWork(object state)
        {
            Average(CoinTime.Time_1day);
            //MACD(CoinTime.Time_1day);
            //KDJ(CoinTime.Time_1day);
        }

        #region 均线系统

        /// <summary>
        ///     均线系统
        /// </summary>
        public void Average(CoinTime type)
        {

            Calc("btc", "usdt", CoinTime.Time_1day);

            //Console.WriteLine("---OutDay---");
            //List<CoinSymbols> allcoin = _huobi.GetAllCoin();

            //foreach (var coin in allcoin)
            //    Calc(coin.base_currency, coin.quote_currency, type);

            //using var scope = _scopeFactory.CreateScope();
            //using var context = scope.ServiceProvider.GetRequiredService<DbcontextBase>();

            //if (ListCoin.Any())
            //{
            //    context.Database.ExecuteSqlRaw("UPDATE DigitalCurrency SET IsDelete=1");
            //    context.DigitalCurrency.AddRange(ListCoin);
            //    context.SaveChanges();
            //    ListCoin.Clear();
            //    Console.WriteLine("---OutDay  SaveChanges---");
            //}
        }

        /// <summary>
        ///     开始计算
        /// </summary>
        /// <param name="type"></param>
        public void Calc(string base_currency, string quote_currency, CoinTime type)
        {
            Console.WriteLine("---Calc---" + base_currency);

            try
            {
                var kline = _huobi.GetKLine(base_currency, quote_currency, type.ToString().Split('_')[1], 2000);

                if (kline == null || kline.Count < 1)
                    return;

                //var ema8 = _indicator.EMA(kline, 9);
                var macd = _indicator.MACD(kline);

                var data5 = _indicator.SMA(kline, 5);
                var data10 = _indicator.SMA(kline, 10);
                var data30 = _indicator.SMA(kline, 30);
                var data60 = _indicator.SMA(kline, 60);


                //获取时间段
                string typename = GetTimeType(type);

                if (data5.Count > 0 && data10.Count > 0 && data5.First().Item2 > data10.First().Item2)
                {
                    Domain.DigitalCurrency.DigitalCurrency model =
                        new Domain.DigitalCurrency.DigitalCurrency
                        {
                            Id = Guid.NewGuid().ToString(),
                            Coin = base_currency.ToUpper() + "-" + quote_currency.ToUpper(),
                            TimeType = typename,
                            CreateTime = DateTime.Now,
                            Platform = "火币",
                            Close = kline.First().close,
                            Open = kline.First().open,
                            High = kline.First().high,
                            Low = kline.First().low
                        };

                    if (data5.Count > 0 && data30.Count > 0 && data5.First().Item2 > data30.First().Item2)
                    {
                        if (data5.Count > 0 && data60.Count > 0 && data5.First().Item2 > data60.First().Item2)
                        {
                            Console.WriteLine(base_currency.ToUpper() + "—" + quote_currency.ToUpper() +
                                              $"【{typename}】突破60K压力位,金叉");
                            model.Desc = "突破60K压力位,金叉";
                        }
                        else
                        {
                            Console.WriteLine(base_currency.ToUpper() + "—" + quote_currency.ToUpper() +
                                              $"【{typename}】突破30K压力位,金叉");
                            model.Desc = "突破30K压力位,金叉";
                        }
                    }
                    else
                    {
                        model.Desc = "突破10K压力位,金叉";
                        Console.WriteLine(base_currency.ToUpper() + "—" + quote_currency.ToUpper() +
                                          $"【{typename}】突破10K压力位,金叉");
                    }

                    model.HighRange = model.High / model.Low - 1;
                    model.CloseRange = model.Close / model.Open - 1;
                    ListCoin.Add(model);
                }
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---Calc---");
            }
        }

        /// <summary>
        ///     获取时间段
        /// </summary>
        /// <param name="type"></param>
        /// <returns></returns>
        public string GetTimeType(CoinTime type)
        {
            switch (type)
            {
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

        #region MACD

        public void MACD(CoinTime type)
        {
            try
            {
                var kline = _huobi.GetKLine("btc", "usdt", type.ToString().Split('_')[1], 300);
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

                var kline = _huobi.GetKLine("btc", "usdt", type.ToString().Split('_')[1], 200);
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

        #endregion 
    }
}