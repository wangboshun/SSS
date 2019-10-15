using System;
using System.Collections.Generic;
using System.Net;
using AutoMapper;
using FluentValidation;
using HtmlAgilityPack;
using Microsoft.Extensions.DependencyInjection;
using Newtonsoft.Json.Linq;
using SSS.Application.Seedwork.Service;
using SSS.Domain.Articel.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Articel;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.DateTime;
using SSS.Infrastructure.Util.Json;

namespace SSS.Application.Articel.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IArticelService))]
    public class ArticelService : QueryService<Domain.Articel.Articel, ArticelInputDto, ArticelOutputDto>,
        IArticelService
    {
        public ArticelService(IMapper mapper,
            IArticelRepository repository,
            IErrorHandler error,
            IValidator<ArticelInputDto> validator) :
            base(mapper, repository, error, validator)
        {
        }

        public void AddArticel(ArticelInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Articel.Articel>(input);
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<ArticelOutputDto>> GetListArticel(ArticelInputDto input)
        {
            return GetList(input);
        }

        public List<ArticelOutputDto> GetNews(ArticelInputDto input)
        {
            WebClient web = new WebClient();
            web.Headers.Add("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Mobile Safari/537.36");
            string json = web.DownloadString("https://api.jinse.com/v6/information/list?catelogue_key=news&limit=23&information_id=0&flag=down&version=9.9.9");
            JToken data = Json.GetJsonValue(json, "list");

            List<ArticelOutputDto> result = new List<ArticelOutputDto>();

            foreach (var item in data.AsJEnumerable())
            {
                ArticelOutputDto model = new ArticelOutputDto();
                model.id = item["id"].ToString();
                model.title = item["title"].ToString();
                GetNewsContnt(item["extra"], model);
                result.Add(model);
            }
            return result;
        }

        public void GetNewsContnt(JToken token, ArticelOutputDto model)
        {
            HtmlWeb htmlWeb = new HtmlWeb();

            HtmlAgilityPack.HtmlDocument document = htmlWeb.Load(token["topic_url"].ToString());

            HtmlNodeCollection nodeCollection = document.DocumentNode.SelectNodes("//div[class=\"js-article-detail\"]");
            //https://www.cnblogs.com/xuliangxing/p/8004403.html
        }

        /// <summary>
        /// 快讯
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public List<ArticelOutputDto> GetQuickNews(ArticelInputDto input)
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
                model.creatrtime = DateTimeConvert.ConvertIntDateTime(item["created_at"].ToString());
                model.title = GetTitle(item["content"].ToString());
                result.Add(model);
            }
            return result;
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