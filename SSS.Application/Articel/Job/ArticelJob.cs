using HtmlAgilityPack;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json.Linq;
using SSS.Domain.Articel.Dto;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.DateTime;
using SSS.Infrastructure.Util.Json;
using System.Collections.Generic;
using System.Net;
using System.Threading;
using System.Threading.Tasks;
using SSS.Infrastructure.Repository.Articel;

namespace SSS.Application.Articel.Job
{
    [DIService(ServiceLifetime.Transient, typeof(IHostedService))]
    public class ArticelJob : BackgroundService
    {
        private readonly ILogger _logger;
        private readonly IServiceScopeFactory _scopeFactory;

        public ArticelJob(ILogger<ArticelJob> logger, IServiceScopeFactory scopeFactory)
        {
            _logger = logger;
            _scopeFactory = scopeFactory;
        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            while (!stoppingToken.IsCancellationRequested)
            {
                GetNews();//15分钟一次新闻
                GetQuickNews();//15分钟一次新闻
                await Task.Delay(1000/* * 60 * 15*/, stoppingToken);
            }
        }

        /// <summary>
        /// 获取新闻
        /// </summary>
        /// <returns></returns>
        public void GetNews()
        {
            WebClient web = new WebClient();
            web.Headers.Add("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Mobile Safari/537.36");
            string json = web.DownloadString("https://api.jinse.com/v6/information/list?catelogue_key=news&limit=23&information_id=0&flag=down&version=9.9.9");
            JToken data = Json.GetJsonValue(json, "list");

            List<Domain.Articel.Articel> result = new List<Domain.Articel.Articel>();

            //using (var scope = _scopeFactory.CreateScope())
            //{
            //    var _repository = scope.ServiceProvider.GetService<IArticelRepository>();
            //    _repository.AddList(result, true);
            //}


            foreach (var item in data.AsJEnumerable())
            {
                Domain.Articel.Articel model = new Domain.Articel.Articel();
                model.Id = item["id"].ToString();
                model.Title = item["title"].ToString();
                GetNewsContnt(item["extra"], model);
                result.Add(model);
            }
          
        }

        /// <summary>
        /// 获取新闻具体内容
        /// </summary>
        /// <param name="token"></param>
        /// <param name="model"></param>
        public void GetNewsContnt(JToken token, Domain.Articel.Articel model)
        {
            HtmlWeb htmlWeb = new HtmlWeb();

            HtmlAgilityPack.HtmlDocument document = htmlWeb.Load(token["topic_url"].ToString());

            HtmlNode node = document.DocumentNode.SelectSingleNode("//div[@id='article-content']");

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

        /// <summary>
        /// 获取快讯
        /// </summary> 
        /// <returns></returns>
        public void GetQuickNews()
        {
            WebClient web = new WebClient();
            web.Headers.Add("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Mobile Safari/537.36");
            string json = web.DownloadString("https://api.jinse.com/v4/live/list?reading=false&sort=7&flag=down&id=0&limit=30");
            JToken data = Json.GetJsonValue(json, "list");
            IJEnumerable<JToken> list = data.First.Last.AsJEnumerable().Values();

            List<ArticelOutputDto> result = new List<ArticelOutputDto>();

            foreach (var item in list)
            {
                ArticelOutputDto model = new ArticelOutputDto();
                model.id = item["id"].ToString();
                model.content = GetDetail(item["content"].ToString());
                model.createtime = DateTimeConvert.ConvertIntDateTimeToString(item["created_at"].ToString());
                model.title = GetTitle(item["content"].ToString());
                result.Add(model);
            }

        }

        /// <summary>
        /// 根据内容获取标题
        /// </summary>
        /// <param name="content"></param>
        /// <returns></returns>
        private string GetTitle(string content)
        {
            int first = content.IndexOf("【");
            int second = content.IndexOf("】");
            return content.Substring(first + 1, second);
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
