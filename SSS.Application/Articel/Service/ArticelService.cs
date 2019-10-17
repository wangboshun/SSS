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
using System;
using System.Collections.Generic;
using System.Linq;

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
            return GetList(x => x.Category == 1, input.pageindex, input.pagesize).data;
        }

        public List<ArticelOutputDto> GetQuickNews(ArticelInputDto input)
        {
            return GetList(x => x.Category == 2, input.pageindex, input.pagesize).data;
        }
    }
}