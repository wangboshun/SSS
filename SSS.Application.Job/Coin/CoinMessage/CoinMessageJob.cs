﻿using HtmlAgilityPack;

using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

using Quartz;
using Quartz.Impl;
using Quartz.Impl.Triggers;

using SSS.Application.Job.JobSetting.Extension;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;

namespace SSS.Application.Job.Coin.CoinMessage
{
    [DIService(ServiceLifetime.Singleton, typeof(CoinMessageJob))]
    public class CoinMessageJob : IJob
    {
        private static readonly object _lock = new object();
        private readonly ILogger _logger;
        private readonly IServiceScopeFactory _scopeFactory;

        public CoinMessageJob(ILogger<CoinMessageJob> logger, IServiceScopeFactory scopeFactory)
        {
            _logger = logger;
            _scopeFactory = scopeFactory;
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

                    GetCoinMessage();

                    watch.Stop();
                    context.Scheduler.Context.Put(trigger.FullName + "_Result", "Success");
                    context.Scheduler.Context.Put(trigger.FullName + "_Time", watch.ElapsedMilliseconds);

                    _logger.LogInformation($"------>{context.GetJobDetail()}  耗时：{watch.ElapsedMilliseconds} ");
                    return Task.FromResult("Success");
                }
                catch (Exception ex)
                {
                    context.Scheduler.Context.Put(trigger.FullName + "_Exception", ex);
                    _logger.LogError(new EventId(ex.HResult), ex, "---CoinMessageJob DoWork Exception---");
                    return Task.FromResult("Error");
                }
            }
        }

        public Task Execute(IJobExecutionContext context)
        {
            _logger.LogInformation("-----------------CoinMessageJob----------------------");
            return DoWork(context);
        }

        /// <summary>
        /// 获取利好新闻消息
        /// </summary>
        public void GetCoinMessage()
        {
            try
            {
                var list = new List<Domain.Coin.CoinMessage.CoinMessage>();
                using var scope = _scopeFactory.CreateScope();
                using var db_context = scope.ServiceProvider.GetRequiredService<CoinDbContext>();
                var source = db_context.CoinMessage.ToList();

                for (var i = 0; i < 10; i++)
                {
                    var htmlWeb = new HtmlWeb();

                    var document = htmlWeb.Load("http://www.biknow.com/?pageNum=" + i);

                    var node = document.DocumentNode.SelectNodes("//div[@class='list']//div[@class='list_con']//div[@class='box']");
                    if (node == null)
                        continue;

                    foreach (var item in node)
                    {
                        var title = item.SelectSingleNode(".//lable").InnerText.Trim();
                        var calendar = item.SelectSingleNode(".//div[@class='time']").InnerText.Trim();
                        var first = title.IndexOf("(");
                        var last = title.IndexOf(")");
                        var coin = title.Substring(first + 1, last - (first + 1)).Trim().ToUpper();

                        if (source.Any(x => x.Coin.Equals(coin) && x.Calendar.Contains(calendar)))
                            continue;

                        if (list.Any(x => x.Coin.Equals(coin) && x.Calendar.Contains(calendar)))
                            continue;

                        var model = new Domain.Coin.CoinMessage.CoinMessage
                        {
                            Id = Guid.NewGuid().ToString(),
                            Content = item.SelectSingleNode(".//h3").InnerText.Trim(),
                            CreateTime = new DateTime(Convert.ToInt32(calendar.Substring(0, 4)),
                                Convert.ToInt32(calendar.Substring(5, 2)),
                                Convert.ToInt32(calendar.Substring(8, 2))),
                            Calendar = calendar,
                            Coin = coin,
                            Title = title
                        };
                        list.Add(model);
                    }
                }

                if (!list.Any()) return;
                db_context.CoinMessage.AddRange(list);
                db_context.SaveChanges();
                Console.WriteLine("---GetGoodNews  SaveChanges---");
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---GetGoodNews Exception---");
            }
        }
    }
}