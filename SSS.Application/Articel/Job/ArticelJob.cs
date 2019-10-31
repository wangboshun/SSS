using HtmlAgilityPack;
 
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

using Newtonsoft.Json.Linq;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.DateTime;
using SSS.Infrastructure.Util.Json;

using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Application.Articel.Job
{
    [DIService(ServiceLifetime.Transient, typeof(IHostedService))]
    public class ArticelJob : IHostedService, IDisposable
    {
        private readonly ILogger _logger;
        private readonly IServiceScopeFactory _scopeFactory;
        private Timer _timer;
        private readonly IHostEnvironment _env;

        public ArticelJob(ILogger<ArticelJob> logger, IServiceScopeFactory scopeFactory, IHostEnvironment env)
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
            //GetNews();
            //GetQuickNews();
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
        /// 获取新闻
        /// </summary>
        /// <returns></returns>
        public void GetNews()
        {
            System.Console.WriteLine("---GetNews---");
            try
            {
                WebClient web = new WebClient();
                string json = web.DownloadString("https://api.jinse.com/v6/information/list?catelogue_key=news&limit=50&information_id=0&flag=down&version=9.9.9");
                JToken data = Json.GetJsonValue(json, "list");

                using var scope = _scopeFactory.CreateScope();
                using var context = scope.ServiceProvider.GetRequiredService<DbcontextBase>();

                var source = context.Articel.ToList();

                List<Domain.Articel.Articel> list = new List<Domain.Articel.Articel>();
                foreach (var item in data.AsJEnumerable())
                {
                    if (source.Any(x => x.Title.Equals(item["title"].ToString())))
                        continue;

                    System.Console.WriteLine("---GetNews---" + item["title"].ToString());

                    Domain.Articel.Articel model = new Domain.Articel.Articel
                    {
                        Id = Guid.NewGuid().ToString(),
                        Title = item["title"].ToString(),
                        Category = 1
                    };

                    GetNewsContnt(item["extra"], model);
                    list.Add(model);
                }
                if (list.Any())
                {
                    context.Articel.AddRange(list);
                    context.SaveChangesAsync();
                    System.Console.WriteLine("---GetNews  SaveChangesAsync---");
                }
            }
            catch (System.Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---GetNews Exception---");
            }
        }

        /// <summary>
        /// 获取新闻具体内容
        /// </summary>
        /// <param name="token"></param>
        /// <param name="model"></param>
        public void GetNewsContnt(JToken token, Domain.Articel.Articel model)
        {
            try
            {
                HtmlWeb htmlWeb = new HtmlWeb();

                HtmlAgilityPack.HtmlDocument document = htmlWeb.Load(token["topic_url"].ToString());

                HtmlNode node = document.DocumentNode.SelectSingleNode("//div[@class='js-article-detail']");

                foreach (var item in node.ChildNodes)
                {
                    if (item.InnerText.Contains("文|") || item.InnerText.Contains("编辑|"))
                        item.InnerHtml = "";
                }

                model.Content = node.InnerHtml;
                model.CreateTime = DateTimeConvert.ConvertDateTime(token["published_at"].ToString());
                model.Logo = token["thumbnail_pic"].ToString();
                model.Author = token["author"].ToString();
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---GetNewsContnt Exception---");
            }

        }

        /// <summary>
        /// 获取快讯
        /// </summary> 
        /// <returns></returns>
        public void GetQuickNews()
        {
            System.Console.WriteLine("---GetQuickNews---");
            try
            {
                WebClient web = new WebClient();
                string json = web.DownloadString("https://api.jinse.com/v4/live/list?reading=false&sort=7&flag=down&id=0&limit=50");
                JToken data = Json.GetJsonValue(json, "list");
                List<JToken> token = new List<JToken>();// data.First.Last.AsJEnumerable().Values();

                for (int i = 0; i < data.Count(); i++)
                {
                    var temp = data[i].Last.AsJEnumerable().Values().ToList();
                    token.AddRange(temp);
                }

                using var scope = _scopeFactory.CreateScope();
                using var context = scope.ServiceProvider.GetRequiredService<DbcontextBase>();
                var source = context.Articel.ToList();

                List<Domain.Articel.Articel> list = new List<Domain.Articel.Articel>();

                foreach (var item in token)
                {
                    var title = GetTitle(item["content"].ToString());

                    System.Console.WriteLine("---GetQuickNews---" + title);

                    if (source.Any(x => x.Title.Equals(title)))
                        continue;

                    Domain.Articel.Articel model = new Domain.Articel.Articel
                    {
                        Id = Guid.NewGuid().ToString(),
                        Content = GetDetail(item["content"].ToString()),
                        CreateTime = DateTimeConvert.ConvertDateTime(item["created_at"].ToString()),
                        Title = title,
                        Category = 2,
                        Logo = ""
                    };
                    list.Add(model);
                }

                if (list.Any())
                {
                    context.Articel.AddRange(list);
                    context.SaveChangesAsync();
                    System.Console.WriteLine("---GetQuickNews  SaveChangesAsync---");
                }
            }
            catch (System.Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---GetQuickNews Exception---");
            }
        }

        /// <summary>
        /// 根据内容获取标题
        /// </summary>
        /// <param name="content"></param>
        /// <returns></returns>
        private string GetTitle(string content)
        {
            int first = content.IndexOf("分析 | ");
            int second = content.IndexOf("】");
            return content.Substring(first + 4, second - 5);
        }

        /// <summary>
        /// 根据内容获取正文
        /// </summary>
        /// <param name="content"></param>
        /// <returns></returns>
        private string GetDetail(string content)
        {
            int second = content.IndexOf("】");
            return content.Substring(second + 1);
        }
    }
}
