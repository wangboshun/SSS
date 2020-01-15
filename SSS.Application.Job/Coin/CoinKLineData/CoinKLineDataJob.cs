using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

using Newtonsoft.Json.Linq;

using Polly;

using Quartz;
using Quartz.Impl;
using Quartz.Impl.Triggers;

using SSS.Application.Job.JobSetting.Extension;
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

namespace SSS.Application.Job.Coin.CoinKLineData
{
    [DIService(ServiceLifetime.Singleton, typeof(CoinKLineDataJob))]
    public class CoinKLineDataJob : IJob
    {
        private static readonly object _lock = new object();
        private readonly HuobiUtils _huobi;
        private readonly ILogger _logger;
        private readonly IServiceScopeFactory _scopeFactory;

        public CoinKLineDataJob(ILogger<CoinKLineDataJob> logger, HuobiUtils huobi, IServiceScopeFactory scopeFactory)
        {
            _logger = logger;
            _scopeFactory = scopeFactory;
            _huobi = huobi;
        }

        public Task Execute(IJobExecutionContext context)
        {
            _logger.LogInformation("-----------------CoinKLineDataJob----------------------");
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

                    var coin_array = JsonConfig.GetSectionValue("TradeConfig:Coin").Split(',');
                    Parallel.ForEach(coin_array, AddKLineData);

                    watch.Stop();
                    context.Scheduler.Context.Put(trigger.FullName + "_Result", "Success");
                    context.Scheduler.Context.Put(trigger.FullName + "_Time", watch.ElapsedMilliseconds);

                    _logger.LogInformation($"------>{context.GetJobDetail()}  耗时：{watch.ElapsedMilliseconds} ");
                    return Task.FromResult("Success");
                }
                catch (Exception ex)
                {
                    context.Scheduler.Context.Put(trigger.FullName + "_Exception", ex);
                    _logger.LogError(new EventId(ex.HResult), ex, "---CoinKLineDataJob DoWork Exception---");
                    return Task.FromResult("Error");
                }
            }
        }

        public void AddKLineData(string coin)
        {
            try
            {
                using var scope = _scopeFactory.CreateScope();
                using var db_context = scope.ServiceProvider.GetRequiredService<CoinDbContext>();
                var new_list = new List<Domain.Coin.CoinKLineData.CoinKLineData>();
                var old_datatime = new List<DateTime>();

                foreach (CoinTime time in Enum.GetValues(typeof(CoinTime)))
                {
                    var max = db_context.CoinKLineData.Where(x => x.Coin.Equals(coin) &&
 						x.IsDelete == 0 &&
						x.TimeType == (int)time &&
                        x.Platform == (int)Platform.Huobi).OrderByDescending(x => x.DataTime)
                        .FirstOrDefault();
                    var size = 2000;
                    if (max != null)
                        size = GetSize(max.DataTime, time);

                    var kline = "";

                    var retry = Policy.Handle<WebException>().Retry(3, (ex, count, text) =>
                    {
                        _logger.LogError(new EventId(ex.HResult), ex,$"---CoinKLineDataJob GetKLine Exception,进行重试 {count}次---");
                        Thread.Sleep(100);
                    });

                    retry.Execute(() =>
                    {
                        kline = _huobi.GetKLine(coin + "usdt", time.ToString().Split('_')[1], size);
                    });

                    if (string.IsNullOrWhiteSpace(kline)) continue;

                    var jobject = JObject.Parse(kline);

                    var json = jobject?["data"];
                    if (json == null) continue;

                    var list = new List<Domain.Coin.CoinKLineData.CoinKLineData>();

                    foreach (var item in json)
                    {
                        var data_time = DateTimeConvert.ConvertDateTime(item["id"].ToString());

                        var model = new Domain.Coin.CoinKLineData.CoinKLineData
                        {
                            Id = Guid.NewGuid().ToString(),
                            IsDelete = 0,
                            Platform = (int)Platform.Huobi,
                            Coin = coin,
                            TimeType = (int)time,
                            DataTime = data_time,
                            Open = Convert.ToDouble(item["open"]),
                            Close = Convert.ToDouble(item["close"]),
                            Low = Convert.ToDouble(item["low"]),
                            High = Convert.ToDouble(item["high"]),
                            CreateTime = DateTime.Now
                        };
                        old_datatime.Add(data_time);
                        list.Add(model);
                    }

                    var old_list = db_context.CoinKLineData.Where(x => old_datatime.Contains(x.DataTime) &&
                                                                       x.TimeType == (int)time &&
                                                                       x.Coin.Equals(coin) &&
                                                                       x.Platform == (int)Platform.Huobi &&
                                                                       x.IsDelete == 0).ToList();

                    if (old_list.Count > 0)
                        db_context.CoinKLineData.RemoveRange(old_list);

                    new_list.AddRange(list);
                }

                db_context.CoinKLineData.AddRange(new_list);
                db_context.SaveChanges();
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---AddKLineData Exception---");
            }
        }

        private int GetSize(DateTime datatime, CoinTime timetype)
        {
            var number = 0;
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

            if (number >= 2000)
                return 2000;
            return number + 1;
        }
    }
}