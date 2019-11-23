using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

using SSS.DigitalCurrency.Domain;
using SSS.DigitalCurrency.Huobi;
using SSS.DigitalCurrency.Indicator;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Json;

using System;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Application.Trade
{
    [DIService(ServiceLifetime.Transient, typeof(IHostedService))]
    public class TradeJob : IHostedService, IDisposable
    {
        private readonly HuobiUtils _huobi;
        private readonly Indicator _indicator;
        private readonly ILogger _logger;
        private readonly IServiceScopeFactory _scopeFactory;

        private Timer _timer;
        public TradeJob(ILogger<TradeJob> logger, HuobiUtils huobi, IServiceScopeFactory scopeFactory, Indicator indicator)
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

        public Task StartAsync(CancellationToken cancellationToken)
        {
            _timer = new Timer(Futures, null, TimeSpan.Zero,
            TimeSpan.FromMinutes(1));

            return Task.CompletedTask;
        }
        public Task StopAsync(CancellationToken cancellationToken)
        {
            return Task.CompletedTask;
        }

        /// <summary>
        /// 合约分析
        /// </summary>
        public void Futures(object state)
        {
            try
            {
                //币种
                string coin = "btc";

                //K线
                var kline = _huobi.GetKLine(coin, "usdt", CoinTime.Time_15min.ToString().Split('_')[1], 2000);

                if (kline == null || kline.Count < 1)
                {
                    _logger.LogError("---K线获取失败---");
                    return;
                }

                //目前收盘价
                double current_price = kline[0].close;

                //均线
                var data5 = _indicator.SMA(kline, 5);
                var data10 = _indicator.SMA(kline, 10);

                //macd
                var macd = _indicator.MACD(kline);

                //kdj
                var kdj = _indicator.KDJ(kline);

                //均线是否金叉    5日线>10日线
                bool avg_status = data5.First()?.Item2 > data10.First()?.Item2;

                _logger.LogInformation($"均线指标    时间：{data5.First()?.Item1} ，5日线{data5.First()?.Item2}，10日线{data10.First()?.Item2}   状态：{avg_status}");

                //macd是否金叉   macd>0 && dif>dea
                bool macd_status =/* macd.FirstOrDefault().Item4 > 0 &&*/ macd.FirstOrDefault()?.Item2 > macd.FirstOrDefault()?.Item3;

                _logger.LogInformation($"macd指标   时间：{macd.FirstOrDefault()?.Item1} , macd:{macd.FirstOrDefault()?.Item4} , dif:{macd.FirstOrDefault()?.Item2}， dea:{macd.FirstOrDefault()?.Item3}   状态：{macd_status}");

                //kdj是否金叉    j<20  && k>d
                bool kdj_status =/* kdj.FirstOrDefault()?.Item4 < 20 &&*/ kdj.FirstOrDefault()?.Item2 > kdj.FirstOrDefault()?.Item3;

                _logger.LogInformation($"kdj指标    时间：{kdj.FirstOrDefault()?.Item1} ，j:{kdj.FirstOrDefault()?.Item4} , k:{kdj.FirstOrDefault()?.Item2}， d:{kdj.FirstOrDefault()?.Item3}   状态：{kdj_status}");

                //三线金叉
                if (avg_status && macd_status && kdj_status)
                {
                    //做多
                    DoBuy(coin, current_price);
                }
                //三线死叉
                else if (!avg_status && !macd_status && !kdj_status)
                {
                    //做空
                    DoSell(coin, current_price);
                }
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---Futures Exception---");
            }
        }

        /// <summary>
        /// 做空
        /// </summary>
        private void DoSell(string coin, double price)
        {
            using var scope = _scopeFactory.CreateScope();
            using var context = scope.ServiceProvider.GetRequiredService<DbcontextBase>();

            var trade = context.Trade.FirstOrDefault(x => x.Coin.Equals(coin) && x.Status == 1 && x.Direction.Equals("做空"));
            if (trade != null)
                return;

            var ping = context.Trade.FirstOrDefault(x => x.Coin.Equals(coin) && x.Status == 1 && x.Direction.Equals("做多"));
            if (ping != null)
                Ping(ping.Id, price);

            SSS.Domain.Trade.Trade model = new Domain.Trade.Trade()
            {
                Id = Guid.NewGuid().ToString(),
                Coin = coin,
                CreateTime = DateTime.Now,
                IsDelete = 0,
                Direction = "做空",
                First_Price = price,
                Size = 100,
                Status = 1,
                UserId = Guid.NewGuid().ToString()
            };

            context.Trade.Add(model);
            context.SaveChanges();

            _logger.LogInformation($"做空成功：{model.ToJson()}");
        }

        /// <summary>
        /// 做多
        /// </summary>
        private void DoBuy(string coin, double price)
        {
            using var scope = _scopeFactory.CreateScope();
            using var context = scope.ServiceProvider.GetRequiredService<DbcontextBase>();

            var trade = context.Trade.FirstOrDefault(x => x.Coin.Equals(coin) && x.Status == 1 && x.Direction.Equals("做多"));
            if (trade != null)
                return;

            var ping = context.Trade.FirstOrDefault(x => x.Coin.Equals(coin) && x.Status == 1 && x.Direction.Equals("做空"));
            if (ping != null)
                Ping(ping.Id, price);

            SSS.Domain.Trade.Trade model = new Domain.Trade.Trade()
            {
                Id = Guid.NewGuid().ToString(),
                Coin = coin,
                CreateTime = DateTime.Now,
                IsDelete = 0,
                Direction = "做多",
                First_Price = price,
                Size = 100,
                Status = 1,
                UserId = Guid.NewGuid().ToString()
            };
            context.Trade.Add(model);
            context.SaveChanges();

            _logger.LogInformation($"做多成功：{model.ToJson()}");
        }

        /// <summary>
        /// 平仓
        /// </summary>
        /// <param name="id"></param>
        /// <param name="price"></param>
        private void Ping(string id, double price)
        {
            using var scope = _scopeFactory.CreateScope();
            using var context = scope.ServiceProvider.GetRequiredService<DbcontextBase>();
            context.Database.ExecuteSqlRaw("UPDATE Trade SET Status=2,Last_Price={0},UpdateTime=Now()  where Id={1}", price, id);
            context.SaveChanges();

            _logger.LogInformation($"---订单：{id}，平单成功---");
        }
    }
}
