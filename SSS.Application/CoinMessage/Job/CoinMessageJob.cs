namespace SSS.Application.CoinMessage.Job
{
    using global::SSS.Infrastructure.Seedwork.DbContext;
    using global::SSS.Infrastructure.Util.Attribute;

    using HtmlAgilityPack;

    using Microsoft.Extensions.DependencyInjection;
    using Microsoft.Extensions.Hosting;
    using Microsoft.Extensions.Logging;

    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading;
    using System.Threading.Tasks;

    namespace SSS.Application.CoinMessageJob.Job
    {
        [DIService(ServiceLifetime.Transient, typeof(IHostedService))]
        public class CoinMessageJob : IHostedService, IDisposable
        {
            private readonly ILogger _logger;
            private readonly IServiceScopeFactory _scopeFactory;
            private Timer _timer;
            private readonly IHostEnvironment _env;

            public CoinMessageJob(ILogger<CoinMessageJob> logger, IServiceScopeFactory scopeFactory, IHostEnvironment env)
            {
                _logger = logger;
                _env = env;
                _scopeFactory = scopeFactory;
            }

            public Task StartAsync(CancellationToken stoppingToken)
            {
                _timer = new Timer(DoWork, null, TimeSpan.Zero,
                    TimeSpan.FromMinutes(100));

                return Task.CompletedTask;
            }

            private void DoWork(object state)
            {
                //GetCoinMessage();
            }

            public Task StopAsync(CancellationToken stoppingToken)
            {
                _timer?.Change(Timeout.Infinite, 0);

                return Task.CompletedTask;
            }

            public void Dispose()
            {
                _timer?.Dispose();
            }

            /// <summary>
            /// 获取利好新闻消息
            /// </summary>
            public void GetCoinMessage()
            {
                try
                {
                    List<Domain.CoinMessage.CoinMessage> list = new List<Domain.CoinMessage.CoinMessage>();
                    using var scope = _scopeFactory.CreateScope();
                    using var context = scope.ServiceProvider.GetRequiredService<DbcontextBase>();
                    var source = context.CoinMessage.ToList();

                    for (int i = 0; i < 10; i++)
                    {
                        HtmlWeb htmlWeb = new HtmlWeb();

                        HtmlAgilityPack.HtmlDocument document = htmlWeb.Load("http://www.biknow.com/?pageNum=" + i);

                        var node = document.DocumentNode.SelectNodes("//div[@class='list']//div[@class='list_con']//div[@class='box']");
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

                            Domain.CoinMessage.CoinMessage model = new Domain.CoinMessage.CoinMessage
                            {
                                Id = Guid.NewGuid().ToString(),
                                Content = item.SelectSingleNode(".//h3").InnerText.Trim(),
                                CreateTime = DateTime.Now,
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
                        context.SaveChangesAsync();
                        System.Console.WriteLine("---GetGoodNews  SaveChangesAsync---");
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
