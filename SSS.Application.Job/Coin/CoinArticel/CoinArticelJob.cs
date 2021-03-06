﻿using HtmlAgilityPack;

using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

using Newtonsoft.Json.Linq;

using Quartz;
using Quartz.Impl;
using Quartz.Impl.Triggers;

using SSS.Application.Job.JobSetting.Extension;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.DateTime;
using SSS.Infrastructure.Util.Json;

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Net;
using System.Threading.Tasks;

namespace SSS.Application.Job.Coin.CoinArticel
{
    [DIService(ServiceLifetime.Singleton, typeof(CoinArticelJob))]
    public class CoinArticelJob : IJob
    {
        private static readonly object _lock = new object();
        private readonly ILogger _logger;
        private readonly IServiceScopeFactory _scopeFactory;

        public CoinArticelJob(ILogger<CoinArticelJob> logger, IServiceScopeFactory scopeFactory)
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

                    var t1 = Task.Factory.StartNew(GetNotice);
                    var t2 = Task.Factory.StartNew(GetPolicy);
                    var t3 = Task.Factory.StartNew(GetNews);
                    var t4 = Task.Factory.StartNew(GetQuickNews);
                    Task.WaitAll(t1, t2, t3, t4);

                    watch.Stop();
                    context.Scheduler.Context.Put(trigger.FullName + "_Result", "Success");
                    context.Scheduler.Context.Put(trigger.FullName + "_Time", watch.ElapsedMilliseconds);

                    _logger.LogInformation($"------>{context.GetJobDetail()}  耗时：{watch.ElapsedMilliseconds} ");
                    return Task.FromResult("Success");
                }
                catch (Exception ex)
                {
                    context.Scheduler.Context.Put(trigger.FullName + "_Exception", ex);
                    _logger.LogError(new EventId(ex.HResult), ex, "---CoinArticelJob DoWork Exception---");
                    return Task.FromResult("Error");
                }
            }
        }

        public Task Execute(IJobExecutionContext context)
        {
            _logger.LogInformation("-----------------CoinArticelJob----------------------");
            return DoWork(context);
        }

        #region 公告频道

        /// <summary>
        /// 公告频道
        /// </summary>
        public void GetNotice()
        {
            Console.WriteLine("---GetNotice---");
            try
            {
                var web = new WebClient();
                var json = web.DownloadString("https://api.jinse.com/v4/live/list?limit=50&reading=false&source=web&sort=5&flag=down&id=0");
                JToken data = json.GetJsonValue("list");
                var token = new List<JToken>(); // data.First.Last.AsJEnumerable().Values();

                for (var i = 0; i < data.Count(); i++)
                {
                    var temp = data[i].Last.AsJEnumerable().Values().ToList();
                    token.AddRange(temp);
                }

                using var scope = _scopeFactory.CreateScope();
                using var db_context = scope.ServiceProvider.GetRequiredService<CoinDbContext>();
                var source = db_context.CoinArticel.ToList();

                var list = new List<Domain.Coin.CoinArticel.CoinArticel>();

                Parallel.ForEach(token, item =>
                {
                    var title = GetTitleByContent(item["content"].ToString());

                    Console.WriteLine("---GetNotice---" + title);

                    if (source.Any(x => x.Title.Equals(title)))
                        return;

                    if (list.Any(x => x.Title.Equals(title)))
                        return;

                    var model = new Domain.Coin.CoinArticel.CoinArticel
                    {
                        Id = Guid.NewGuid().ToString(),
                        Content = GetDetail(item["content"].ToString()),
                        CreateTime = DateTimeConvert.ConvertDateTime(item["created_at"].ToString()),
                        Title = title,
                        Category = 4,
                        Logo = ""
                    };
                    list.Add(model);
                });

                if (!list.Any()) return;
                db_context.CoinArticel.AddRange(list);
                db_context.SaveChanges();
                Console.WriteLine("---GetNotice  SaveChanges---");
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---GetNotice Exception---");
            }
        }

        #endregion 公告频道

        #region 政策频道

        /// <summary>
        /// 政策新闻
        /// </summary>
        public void GetPolicy()
        {
            Console.WriteLine("---GetPolicy---");
            try
            {
                var web = new WebClient();
                var json = web.DownloadString("https://api.jinse.com/v6/information/list?catelogue_key=zhengce&limit=50&information_id=0&flag=down&version=9.9.9");
                JToken data = json.GetJsonValue("list");

                using var scope = _scopeFactory.CreateScope();
                using var db_context = scope.ServiceProvider.GetRequiredService<CoinDbContext>();

                var source = db_context.CoinArticel.ToList();

                var list = new List<Domain.Coin.CoinArticel.CoinArticel>();

                Parallel.ForEach(data.AsJEnumerable(), item =>
                {
                    var title = GetTitle(item["title"].ToString());

                    if (source.Any(x => x.Title.Equals(title)))
                        return;

                    if (list.Any(x => x.Title.Equals(title)))
                        return;

                    Console.WriteLine("---GetPolicy---" + title);

                    var model = new Domain.Coin.CoinArticel.CoinArticel
                    {
                        Id = Guid.NewGuid().ToString(),
                        Title = title,
                        Category = 3
                    };

                    GetNewsContnt(item["extra"], model);
                    list.Add(model);
                });

                if (!list.Any()) return;
                db_context.CoinArticel.AddRange(list);
                db_context.SaveChanges();
                Console.WriteLine("---GetPolicy  SaveChanges---");
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---GetPolicy Exception---");
            }
        }

        #endregion 政策频道

        #region 快讯频道

        /// <summary>
        /// 快讯频道
        /// </summary>
        /// <returns></returns>
        public void GetQuickNews()
        {
            Console.WriteLine("---GetQuickNews---");
            try
            {
                var web = new WebClient();
                var json = web.DownloadString("https://api.jinse.com/v4/live/list?reading=false&sort=7&flag=down&id=0&limit=50");
                JToken data = json.GetJsonValue("list");
                var token = new List<JToken>(); // data.First.Last.AsJEnumerable().Values();

                for (var i = 0; i < data.Count(); i++)
                {
                    var temp = data[i].Last.AsJEnumerable().Values().ToList();
                    token.AddRange(temp);
                }

                using var scope = _scopeFactory.CreateScope();
                using var db_context = scope.ServiceProvider.GetRequiredService<CoinDbContext>();
                var source = db_context.CoinArticel.ToList();

                var list = new List<Domain.Coin.CoinArticel.CoinArticel>();

                Parallel.ForEach(token, item =>
                {
                    var title = GetTitleByContent(item["content"].ToString());

                    Console.WriteLine("---GetQuickNews---" + title);

                    if (source.Any(x => x.Title.Equals(title)))
                        return;

                    if (list.Any(x => x.Title.Equals(title)))
                        return;

                    var model = new Domain.Coin.CoinArticel.CoinArticel
                    {
                        Id = Guid.NewGuid().ToString(),
                        Content = GetDetail(item["content"].ToString()),
                        CreateTime = DateTimeConvert.ConvertDateTime(item["created_at"].ToString()),
                        Title = title,
                        Category = 2,
                        Logo = ""
                    };
                    list.Add(model);
                });

                if (!list.Any()) return;
                db_context.CoinArticel.AddRange(list);
                db_context.SaveChanges();
                Console.WriteLine("---GetQuickNews  SaveChanges---");
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---GetQuickNews Exception---");
            }
        }

        #endregion 快讯频道

        #region 新闻频道

        /// <summary>
        /// 热点新闻
        /// </summary>
        /// <returns></returns>
        public void GetNews()
        {
            Console.WriteLine("---GetNews---");
            try
            {
                var web = new WebClient();
                var json = web.DownloadString("https://api.jinse.com/v6/information/list?catelogue_key=news&limit=50&information_id=0&flag=down&version=9.9.9");
                JToken data = json.GetJsonValue("list");

                using var scope = _scopeFactory.CreateScope();
                using var db_context = scope.ServiceProvider.GetRequiredService<CoinDbContext>();

                var source = db_context.CoinArticel.ToList();

                var list = new List<Domain.Coin.CoinArticel.CoinArticel>();

                Parallel.ForEach(data.AsJEnumerable(), item =>
                {
                    var title = GetTitle(item["title"].ToString());

                    if (source.Any(x => x.Title.Equals(title)))
                        return;

                    if (list.Any(x => x.Title.Equals(title)))
                        return;

                    Console.WriteLine("---GetNews---" + title);

                    var model = new Domain.Coin.CoinArticel.CoinArticel
                    {
                        Id = Guid.NewGuid().ToString(),
                        Title = title,
                        Category = 1
                    };

                    GetNewsContnt(item["extra"], model);
                    list.Add(model);
                });

                if (!list.Any()) return;
                db_context.CoinArticel.AddRange(list);
                db_context.SaveChanges();
                Console.WriteLine("---GetNews  SaveChanges---");
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---GetNews Exception---");
            }
        }

        /// <summary>
        /// 获取新闻具体内容
        /// </summary>
        /// <param name="token"></param>
        /// <param name="model"></param>
        public void GetNewsContnt(JToken token, Domain.Coin.CoinArticel.CoinArticel model)
        {
            try
            {
                var htmlWeb = new HtmlWeb();

                var document = htmlWeb.Load(token["topic_url"].ToString());

                var node = document.DocumentNode.SelectSingleNode("//div[@class='js-article-detail']") ??
                           document.DocumentNode.SelectSingleNode("//div[@class='js-article']");

                if (node != null)
                {
                    foreach (var item in node.ChildNodes)
                        if (item.InnerText.Contains("文|") || item.InnerText.Contains("编辑|"))
                            item.InnerHtml = "";

                    model.Content = node.InnerHtml;
                }

                model.CreateTime = DateTimeConvert.ConvertDateTime(token["published_at"].ToString());
                var logo = "http://pic.51yuansu.com/pic3/cover/02/61/11/59fc30d0b8598_610.jpg";
                if (!string.IsNullOrWhiteSpace(token["thumbnail_pic"].ToString()))
                    logo = token["thumbnail_pic"].ToString();

                model.Logo = logo;
                model.Author = token["author"].ToString();
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---GetNewsContnt Exception---");
            }
        }

        #endregion 新闻频道

        #region 去除敏感信息

        /// <summary>
        /// 根据Title获取标题
        /// </summary>
        /// <param name="title"></param>
        /// <returns></returns>
        public string GetTitle(string title)
        {
            if (title.IndexOf("金色") <= -1 || (title.IndexOf("丨") <= -1 && title.IndexOf("|") <= -1))
                return title.Trim();
            var index = title.IndexOf("丨");
            if (index < 0)
                index = title.IndexOf("|");
            title = title.Substring(index + 1);

            return title.Trim();
        }

        /// <summary>
        /// 根据内容获取正文
        /// </summary>
        /// <param name="content"></param>
        /// <returns></returns>
        private string GetDetail(string content)
        {
            var second = content.IndexOf("】");
            return content.Substring(second + 1);
        }

        /// <summary>
        /// 根据内容获取标题
        /// </summary>
        /// <param name="content"></param>
        /// <returns></returns>
        private string GetTitleByContent(string content)
        {
            var first = content.IndexOf(" | ");

            var second = content.IndexOf("】");
            return content.Substring(first + 2, second - 5).Trim();
        }

        #endregion 去除敏感信息
    }
}