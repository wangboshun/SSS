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
using SSS.Infrastructure.Util.DI;
using SSS.Infrastructure.Util.Json;

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;

namespace SSS.Application.Job.Coin.CoinTrade
{
    [DIService(ServiceLifetime.Singleton, typeof(CoinTradeJob))]
    public class CoinTradeJob : IJob
    {
        private readonly Indicator _indicator;
        private readonly ILogger _logger;
        private static object _lock = new object();

        public CoinTradeJob(ILogger<CoinTradeJob> logger, Indicator indicator)
        {
            _logger = logger;
            _indicator = indicator;
        }

        public Task Execute(IJobExecutionContext context)
        {
            _logger.LogInformation("-----------------CoinTradeJob----------------------");
            return DoWork(context);
        }

        public Task DoWork(IJobExecutionContext context)
        {
            lock (_lock)
            {
                var trigger = (Quartz.Impl.Triggers.CronTriggerImpl)((Quartz.Impl.JobExecutionContextImpl)context).Trigger;
                try
                {
                    Stopwatch watch = new Stopwatch();
                    watch.Start();

                    Futures();

                    watch.Stop();
                    context.Scheduler.Context.Put(trigger.FullName + "_Result", "Success");
                    context.Scheduler.Context.Put(trigger.FullName + "_Time", watch.ElapsedMilliseconds);

                    _logger.LogInformation($"------>{context.GetJobDetail()}  耗时：{watch.ElapsedMilliseconds} ");
                    return Task.FromResult("Success");
                }
                catch (Exception ex)
                {
                    context.Scheduler.Context.Put(trigger.FullName + "_Exception", ex);
                    _logger.LogError(new EventId(ex.HResult), ex, "---CoinTradeJob DoWork Exception---");
                    return Task.FromResult("Error");
                }
            }
        }

        /// <summary>
        ///     合约分析
        /// </summary>
        public void Futures()
        {
            try
            {
                var db_context = IocEx.Instance.GetRequiredService<CoinDbContext>();

                var coin_kline_data = new Dictionary<string, List<Domain.Coin.CoinKLineData.CoinKLineData>>();

                string[] coin_array = JsonConfig.GetSectionValue("TradeConfig:Coin").Split(',');

                foreach (var coin in coin_array)
                {
                    foreach (CoinTime time in Enum.GetValues(typeof(CoinTime)))
                    {
                        var kline = db_context.CoinKLineData.Where(x => x.Coin.Equals(coin) && x.TimeType == (int)time && x.IsDelete == 0).OrderByDescending(x => x.DataTime).Take(2000).ToList();
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
                var db_context = IocEx.Instance.GetRequiredService<CoinDbContext>();

                var cointrade = db_context.CoinTrade.FirstOrDefault(x => x.Coin.Equals(coin) &&
                                                                      x.Status == 1 &&
                                                                      x.Direction.Equals("做空") &&
                                                                      x.QuantType == quanttype &&
                                                                      x.TimeType == timetype);
                if (cointrade != null)
                    return;

                var ping = db_context.CoinTrade.FirstOrDefault(x => x.Coin.Equals(coin) &&
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

                db_context.CoinTrade.Add(model);
                db_context.SaveChanges();

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
                var db_context = IocEx.Instance.GetRequiredService<CoinDbContext>();

                var cointrade = db_context.CoinTrade.FirstOrDefault(x => x.Coin.Equals(coin) &&
                                                                      x.Status == 1 &&
                                                                      x.Direction.Equals("做多") &&
                                                                      x.QuantType == quanttype &&
                                                                      x.TimeType == timetype);
                if (cointrade != null)
                    return;

                var ping = db_context.CoinTrade.FirstOrDefault(x => x.Coin.Equals(coin) &&
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
                db_context.CoinTrade.Add(model);
                db_context.SaveChanges();

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
            var db_context = IocEx.Instance.GetRequiredService<CoinDbContext>();
            db_context.Database.ExecuteSqlRaw("UPDATE CoinTrade SET Status=2,Last_Price={0},UpdateTime=Now()  where Id={1}", price, id);
            db_context.SaveChanges();

            _logger.LogInformation($"---订单：{id}，平单成功---");
        }
    }
}