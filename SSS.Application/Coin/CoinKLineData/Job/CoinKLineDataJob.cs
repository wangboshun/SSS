using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

using Newtonsoft.Json.Linq;

using Polly;

using SSS.DigitalCurrency.Domain;
using SSS.DigitalCurrency.Huobi;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Config;
using SSS.Infrastructure.Util.DateTime;

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Net;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Application.Coin.CoinKLineData.Job
{
    [DIService(ServiceLifetime.Transient, typeof(IHostedService))]
    public class CoinKLineDataJob : IHostedService, IDisposable
    {
        private readonly HuobiUtils _huobi;
        private readonly ILogger _logger;
        private readonly IServiceScopeFactory _scopeFactory;
        private static readonly object _lock = new object();

        private Timer _timer;

        public CoinKLineDataJob(ILogger<CoinKLineDataJob> logger, HuobiUtils huobi, IServiceScopeFactory scopeFactory)
        {
            _logger = logger;
            _scopeFactory = scopeFactory;
            _huobi = huobi;
        }

        public Task StartAsync(CancellationToken cancellationToken)
        {
            _timer = new Timer(DoWork, null, TimeSpan.Zero, TimeSpan.FromMinutes(1));

            return Task.CompletedTask;
        }

        private void DoWork(object state)
        {
            lock (_lock)
            {
                Stopwatch watch = new Stopwatch();
                watch.Start();

                string[] coin_array = JsonConfig.GetSectionValue("TradeConfig:Coin").Split(',');

                Parallel.ForEach(coin_array, AddKLineData);

                watch.Stop();
                _logger.LogDebug($" CoinKLineDataJob  RunTime {watch.ElapsedMilliseconds} ");
            }
        }

        public Task StopAsync(CancellationToken cancellationToken)
        {
            return Task.CompletedTask;
        }

        public void Dispose()
        {
            _timer?.Dispose();
        }

        public void AddKLineData(string coin)
        {
            return;
            try
            {
                using var scope = _scopeFactory.CreateScope();
                using var context = scope.ServiceProvider.GetRequiredService<DbcontextBase>();
                List<Domain.Coin.CoinKLineData.CoinKLineData> old_list = new List<Domain.Coin.CoinKLineData.CoinKLineData>();

                foreach (CoinTime time in Enum.GetValues(typeof(CoinTime)))
                {
                    var max = context.CoinKLineData.Where(x => x.Coin.Equals(coin) && x.IsDelete == 0 && x.Timetype == (int)time && x.Platform == (int)Platform.Huobi).OrderByDescending(x => x.Datatime).FirstOrDefault();
                    int size = 2000;
                    if (max != null)
                        size = GetSize(max.Datatime, time);

                    string kline = "";

                    var retry = Policy.Handle<WebException>().Retry(3, (ex, count, text) =>
                    {
                        _logger.LogError(new EventId(ex.HResult), ex, $"--- GetKLine Exception,进行重试 {count}次---");
                        Thread.Sleep(100);
                    });

                    retry.Execute(() =>
                    {
                        kline = _huobi.GetKLine(coin + "usdt", time.ToString().Split('_')[1], size);
                    });

                    if (string.IsNullOrWhiteSpace(kline)) continue;

                    JObject jobject = JObject.Parse(kline);

                    var json = jobject?["data"];
                    if (json == null) continue;

                    List<Domain.Coin.CoinKLineData.CoinKLineData> list = new List<Domain.Coin.CoinKLineData.CoinKLineData>();

                    foreach (var item in json)
                    {
                        var data_time = DateTimeConvert.ConvertDateTime(item["id"].ToString());

                        var old_data = context.CoinKLineData.Where(x => x.Coin.Equals(coin) && x.IsDelete == 0 && x.Timetype == (int)time && x.Platform == (int)Platform.Huobi && x.Datatime == data_time);

                        if (old_data.Count() > 0)
                            old_list.AddRange(old_data);

                        Domain.Coin.CoinKLineData.CoinKLineData model = new Domain.Coin.CoinKLineData.CoinKLineData
                        {
                            Id = Guid.NewGuid().ToString(),
                            IsDelete = 0,
                            Platform = (int)Platform.Huobi,
                            Coin = coin,
                            Timetype = (int)time,
                            Datatime = data_time,
                            Open = Convert.ToDouble(item["open"]),
                            Close = Convert.ToDouble(item["close"]),
                            Low = Convert.ToDouble(item["low"]),
                            High = Convert.ToDouble(item["high"]),
                            CreateTime = DateTime.Now
                        };
                        list.Add(model);
                    }

                    context.CoinKLineData.AddRange(list);
                }
                context.CoinKLineData.RemoveRange(old_list);
                context.SaveChanges();
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---AddKLineData Exception---");
            }
        }

        private int GetSize(DateTime datatime, CoinTime timetype)
        {
            int number = 0;
            switch (timetype)
            {
                case CoinTime.Time_1min:
                    number = (int)(DateTime.Now - datatime).TotalMinutes;
                    break;
                case CoinTime.Time_5min:
                    number = (int)(DateTime.Now - datatime).TotalMinutes;
                    if (number < 5)
                        return 1;
                    number /= 5;
                    break;
                case CoinTime.Time_15min:
                    number = (int)(DateTime.Now - datatime).TotalMinutes;
                    if (number < 15)
                        return 1;
                    number /= 15;
                    break;
                case CoinTime.Time_60min:
                    number = (int)(DateTime.Now - datatime).TotalHours;
                    break;
                case CoinTime.Time_4hour:
                    number = (int)(DateTime.Now - datatime).TotalHours;
                    if (number < 4)
                        return 1;
                    number /= 4;
                    break;
                case CoinTime.Time_1day:
                    number = (int)(DateTime.Now - datatime).TotalDays;
                    break;
            }

            return number + 1;
        }
    }
}
