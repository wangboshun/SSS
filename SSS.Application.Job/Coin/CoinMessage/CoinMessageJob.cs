using HtmlAgilityPack;

using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

using Quartz;

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
    namespace SSS.Application.Coin.CoinMessageJob.Job
    {
        [DIService(ServiceLifetime.Transient, typeof(CoinMessageJob))]
        public class CoinMessageJob : IJob
        {
            private readonly ILogger _logger;
            private readonly IServiceScopeFactory _scopeFactory;

            public CoinMessageJob(ILogger<CoinMessageJob> logger, IServiceScopeFactory scopeFactory)
            {
                _logger = logger;
                _scopeFactory = scopeFactory;
            }

            public Task Execute(IJobExecutionContext context)
            {
                _logger.LogInformation("-----------------CoinMessageJob----------------------");
                DoWork(context);
                return Task.FromResult("Success");
            }

            public void DoWork(IJobExecutionContext context)
            {
                Stopwatch watch = new Stopwatch();
                watch.Start();

                GetCoinMessage();

                watch.Stop();
                _logger.LogInformation($"------>{context.GetJobDetail()}  耗时：{watch.ElapsedMilliseconds} ");
            }

            /// <summary>
            ///     获取利好新闻消息
            /// </summary>
            public void GetCoinMessage()
            {
                try
                {
                    List<Domain.Coin.CoinMessage.CoinMessage> list = new List<Domain.Coin.CoinMessage.CoinMessage>();
                    using var scope = _scopeFactory.CreateScope();
                    using var context = scope.ServiceProvider.GetRequiredService<CoinDbContext>();
                    var source = context.CoinMessage.ToList();

                    for (int i = 0; i < 10; i++)
                    {
                        HtmlWeb htmlWeb = new HtmlWeb();

                        HtmlDocument document = htmlWeb.Load("http://www.biknow.com/?pageNum=" + i);

                        var node = document.DocumentNode.SelectNodes(
                            "//div[@class='list']//div[@class='list_con']//div[@class='box']");
                        if (node == null)
                            continue;

                        foreach (var item in node)
                        {
                            string title = item.SelectSingleNode(".//lable").InnerText.Trim();
                            string calendar = item.SelectSingleNode(".//div[@class='time']").InnerText.Trim();
                            int first = title.IndexOf("(");
                            int last = title.IndexOf(")");
                            string coin = title.Substring(first + 1, last - (first + 1)).Trim().ToUpper();

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

                    if (list.Any())
                    {
                        context.CoinMessage.AddRange(list);
                        context.SaveChanges();
                        Console.WriteLine("---GetGoodNews  SaveChanges---");
                    }
                }
                catch (Exception ex)
                {
                    _logger.LogError(new EventId(ex.HResult), ex, "---GetGoodNews Exception---");
                }
            }
        }
    }
}