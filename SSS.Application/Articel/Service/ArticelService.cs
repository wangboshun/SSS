using System;
using System.Collections.Generic;
using System.Net;
using AutoMapper;
using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using Newtonsoft.Json.Linq;
using SSS.Application.Seedwork.Service;
using SSS.Domain.Articel.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Articel;
using SSS.Infrastructure.Util.Attribute;
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
            string result = web.DownloadString("https://api.jinse.com/v4/live/list?reading=false&sort=7&flag=down&id=0&limit=30");
            JToken data = Json.GetJsonValue(result, "list");
            foreach (var item in data.Last)
            {
                foreach (var val in item)
                {
                    
                }
            }
            return null;
        }
    }
}