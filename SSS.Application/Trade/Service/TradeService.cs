using AutoMapper;
using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Application.Seedwork.Service;
using SSS.Domain.Trade.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Trade;
using SSS.Infrastructure.Util.Attribute;
using System;
using System.Collections.Generic;

namespace SSS.Application.Trade.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(ITradeService))]
    public class TradeService : QueryService<SSS.Domain.Trade.Trade, TradeInputDto, TradeOutputDto>, ITradeService
    {
        public TradeService(IMapper mapper,
            ITradeRepository repository,
            IErrorHandler error,
            IValidator<TradeInputDto> validator) :
            base(mapper, repository, error, validator)
        {

        }

        public void AddTrade(TradeInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Trade.Trade>(input);
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<TradeOutputDto>> GetListTrade(TradeInputDto input)
        {
            return GetPage(input);
        }
    }
}