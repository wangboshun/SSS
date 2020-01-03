using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

using Quartz;

using SSS.Application.Job.JobSetting.Extension;
using SSS.DigitalCurrency.Domain;
using SSS.DigitalCurrency.Indicator;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Config;
using SSS.Infrastructure.Util.Json;

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;

namespace SSS.Application.Job.Coin.CoinTrade
{
    [DIService(ServiceLifetime.Transient, typeof(CoinTradeJob))]
    public class CoinTradeJob : IJob
    {
        private readonly Indicator _indicator;
        private readonly ILogger _logger;
        private readonly IServiceScopeFactory _scopeFactory;

        public CoinTradeJob(ILogger<CoinTradeJob> logger, IServiceScopeFactory scopeFactory, Indicator indicator)
        {
            _logger = logger;
            _indicator = indicator;
            _scopeFactory = scopeFactory;
        }

        public Task Execute(IJobExecutionContext context)
        {
            _logger.LogInformation("-----------------CoinTradeJob----------------------");
            DoWork(context);
            return Task.FromResult("Success");
        }

        public void DoWork(IJobExecutionContext context)
        {
            Stopwatch watch = new Stopwatch();
            watch.Start();

            Futures();

            watch.Stop();
            _logger.LogInformation($"------>{context.GetJobDetail()}  耗时：{watch.ElapsedMilliseconds} ");
        }

        /// <summary>
        ///     合约分析
        /// </summary>
        public void Futures()
        {
            try
            {
                using var scope = _scopeFactory.CreateScope();
                using var context = scope.ServiceProvider.GetRequiredService<CoinDbContext>();

                Dictionary<string, List<Domain.Coin.CoinKLineData.CoinKLineData>> coin_kline_data = new Dictionary<string, List<Domain.Coin.CoinKLineData.CoinKLineData>>();

                string[] coin_array = JsonConfig.GetSectionValue("TradeConfig:Coin").Split(',');

                foreach (var coin in coin_array)
                {
                    foreach (CoinTime time in Enum.GetValues(typeof(CoinTime)))
                    {
                        var kline = context.CoinKLineData.Where(x => x.Coin.Equals(coin) && x.TimeType == (int)time && x.IsDelete == 0).OrderByDescending(x => x.DataTime).Take(2000).ToList();
                        if (kline.Count < 1)
                        {
                            _logger.LogError("---K线获取失败---");
                            continue;
                        }
                        coin_kline_data.Add(coin + "_" + (int)time, kline);
                    }
                }

                //并行计算  订单操作
                Parallel.ForEach(coin_kline_data, (item) =>
                {
                    Trade(item.Key, item.Value);
                });
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---Futures Exception---");
            }
        }

        /// <summary>
        /// 订单处理
        /// </summary> 
        private void Trade(string coin, List<Domain.Coin.CoinKLineData.CoinKLineData> kline)
        {
            try
            {
                var coininfo_array = coin.Split('_');

                //目前收盘价
                double current_price = kline[0].Close;

                //均线是否金叉   
                bool avg_status = Calc_Sma(coin, kline);

                //macd是否金叉  
                bool macd_status = Calc_Macd(coin, kline);

                //kdj是否金叉   
                bool kdj_status = Calc_Kdj(coin, kline);

                foreach (QuantEnum quant in Enum.GetValues(typeof(QuantEnum)))
                {
                    switch (quant)
                    {
                        case QuantEnum.Macd_Sma_Kdj:
                            //三线金叉
                            if (avg_status && macd_status && kdj_status) //做多
                                DoBuy(coininfo_array[0], current_price, Convert.ToInt32(coininfo_array[1]), (int)QuantEnum.Macd_Sma_Kdj);

                            //三线死叉
                            else if (!avg_status && !macd_status && !kdj_status) //做空
                                DoSell(coininfo_array[0], current_price, Convert.ToInt32(coininfo_array[1]), (int)QuantEnum.Macd_Sma_Kdj);
                            break;

                        case QuantEnum.Sma:
                            //Sma金叉
                            if (avg_status) //做多
                                DoBuy(coininfo_array[0], current_price, Convert.ToInt32(coininfo_array[1]), (int)QuantEnum.Sma);

                            //Sma死叉
                            else  //做空
                                DoSell(coininfo_array[0], current_price, Convert.ToInt32(coininfo_array[1]), (int)QuantEnum.Sma);
                            break;

                        case QuantEnum.Macd:
                            //Macd金叉
                            if (macd_status) //做多
                                DoBuy(coininfo_array[0], current_price, Convert.ToInt32(coininfo_array[1]), (int)QuantEnum.Macd);

                            //Macd死叉
                            else  //做空
                                DoSell(coininfo_array[0], current_price, Convert.ToInt32(coininfo_array[1]), (int)QuantEnum.Macd);
                            break;

                        case QuantEnum.Kdj:
                            //Kdj金叉
                            if (kdj_status) //做多
                                DoBuy(coininfo_array[0], current_price, Convert.ToInt32(coininfo_array[1]), (int)QuantEnum.Kdj);

                            //Kdj死叉
                            else  //做空
                                DoSell(coininfo_array[0], current_price, Convert.ToInt32(coininfo_array[1]), (int)QuantEnum.Kdj);
                            break;
                    }
                }
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---Trade Exception---");
            }
        }

        /// <summary>
        /// 均线是否金叉    5日线>10日线
        /// </summary>
        /// <param name="coin"></param>
        /// <param name="kline"></param>
        /// <returns></returns>
        public bool Calc_Sma(string coin, List<Domain.Coin.CoinKLineData.CoinKLineData> kline)
        {
            var data5 = _indicator.SMA(kline, 5);
            var data10 = _indicator.SMA(kline, 10);
            bool avg_status = data5.First()?.Item2 > data10.First()?.Item2;
            _logger.LogInformation($"{coin} 均线指标    时间：{data5.First()?.Item1} ，5日线{data5.First()?.Item2}，10日线{data10.First()?.Item2}   状态：{avg_status}");
            return avg_status;
        }

        /// <summary>
        /// macd是否金叉   macd>0 && dif>dea
        /// </summary>
        /// <param name="coin"></param>
        /// <param name="kline"></param>
        /// <returns></returns>
        public bool Calc_Macd(string coin, List<Domain.Coin.CoinKLineData.CoinKLineData> kline)
        {
            var macd = _indicator.MACD(kline);

            //macd是否金叉   macd>0 && dif>dea
            bool macd_status = /* macd.FirstOrDefault().Item4 > 0 &&*/macd.FirstOrDefault()?.Item2 > macd.FirstOrDefault()?.Item3;
            _logger.LogInformation($"{coin}  macd指标   时间：{macd.FirstOrDefault()?.Item1} , macd:{macd.FirstOrDefault()?.Item4} , dif:{macd.FirstOrDefault()?.Item2}， dea:{macd.FirstOrDefault()?.Item3}   状态：{macd_status}");
            return macd_status;
        }

        /// <summary>
        /// kdj是否金叉    j<20  && k>d
        /// </summary>
        /// <param name="coin"></param>
        /// <param name="kline"></param>
        /// <returns></returns>
        public bool Calc_Kdj(string coin, List<Domain.Coin.CoinKLineData.CoinKLineData> kline)
        {
            var kdj = _indicator.KDJ(kline);
            bool kdj_status = /* kdj.FirstOrDefault()?.Item4 < 20 &&*/kdj.FirstOrDefault()?.Item2 > kdj.FirstOrDefault()?.Item3;
            _logger.LogInformation($"{coin}  kdj指标    时间：{kdj.FirstOrDefault()?.Item1} ，j:{kdj.FirstOrDefault()?.Item4} , k:{kdj.FirstOrDefault()?.Item2}， d:{kdj.FirstOrDefault()?.Item3}   状态：{kdj_status}");
            return kdj_status;
        }

        /// <summary>
        ///     做空
        /// </summary>
        private void DoSell(string coin, double price, int timetype, int quanttype)
        {
            try
            {
                using var scope = _scopeFactory.CreateScope();
                using var context = scope.ServiceProvider.GetRequiredService<CoinDbContext>();

                var cointrade = context.CoinTrade.FirstOrDefault(x => x.Coin.Equals(coin) &&
                                                                      x.Status == 1 &&
                                                                      x.Direction.Equals("做空") &&
                                                                      x.QuantType == quanttype &&
                                                                      x.TimeType == timetype);
                if (cointrade != null)
                    return;

                var ping = context.CoinTrade.FirstOrDefault(x => x.Coin.Equals(coin) &&
                                                                 x.Status == 1 &&
                                                                 x.Direction.Equals("做多") &&
                                                                 x.QuantType == quanttype &&
                                                                 x.TimeType == timetype);
                if (ping != null)
                    Ping(ping.Id, price);

                var model = new Domain.Coin.CoinTrade.CoinTrade
                {
                    Id = Guid.NewGuid().ToString(),
                    Coin = coin,
                    CreateTime = DateTime.Now,
                    IsDelete = 0,
                    Direction = "做空",
                    First_Price = price,
                    Size = 100,
                    QuantType = quanttype,
                    TimeType = timetype,
                    Status = 1,
                    UserId = Guid.NewGuid().ToString()
                };

                context.CoinTrade.Add(model);
                context.SaveChanges();

                _logger.LogInformation($"做空成功：{model.ToJson()}");
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---DoSell Exception---");
            }
        }

        /// <summary>
        ///     做多
        /// </summary>
        private void DoBuy(string coin, double price, int timetype, int quanttype)
        {
            try
            {
                using var scope = _scopeFactory.CreateScope();
                using var context = scope.ServiceProvider.GetRequiredService<CoinDbContext>();

                var cointrade = context.CoinTrade.FirstOrDefault(x => x.Coin.Equals(coin) &&
                                                                      x.Status == 1 &&
                                                                      x.Direction.Equals("做多") &&
                                                                      x.QuantType == quanttype &&
                                                                      x.TimeType == timetype);
                if (cointrade != null)
                    return;

                var ping = context.CoinTrade.FirstOrDefault(x => x.Coin.Equals(coin) &&
                                                                 x.Status == 1 &&
                                                                 x.Direction.Equals("做空") &&
                                                                 x.QuantType == quanttype &&
                                                                 x.TimeType == timetype);
                if (ping != null)
                    Ping(ping.Id, price);

                var model = new Domain.Coin.CoinTrade.CoinTrade
                {
                    Id = Guid.NewGuid().ToString(),
                    Coin = coin,
                    CreateTime = DateTime.Now,
                    IsDelete = 0,
                    Direction = "做多",
                    First_Price = price,
                    Size = 100,
                    QuantType = quanttype,
                    TimeType = timetype,
                    Status = 1,
                    UserId = Guid.NewGuid().ToString()
                };
                context.CoinTrade.Add(model);
                context.SaveChanges();

                _logger.LogInformation($"做多成功：{model.ToJson()}");
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---DoBuy Exception---");
            }
        }

        /// <summary>
        ///     平仓
        /// </summary>
        /// <param name="id"></param>
        /// <param name="price"></param>
        private void Ping(string id, double price)
        {
            using var scope = _scopeFactory.CreateScope();
            using var context = scope.ServiceProvider.GetRequiredService<CoinDbContext>();
            context.Database.ExecuteSqlRaw("UPDATE CoinTrade SET Status=2,Last_Price={0},UpdateTime=Now()  where Id={1}", price, id);
            context.SaveChanges();

            _logger.LogInformation($"---订单：{id}，平单成功---");
        }
    }
}