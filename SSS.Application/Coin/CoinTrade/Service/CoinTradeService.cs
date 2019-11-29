using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Coin.CoinTrade.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Coin.CoinTrade;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Coin.CoinTrade.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(ICoinTradeService))]
    public class CoinTradeService : QueryService<SSS.Domain.Coin.CoinTrade.CoinTrade, CoinTradeInputDto, CoinTradeOutputDto>, ICoinTradeService
    {
        public CoinTradeService(IMapper mapper,
            ICoinTradeRepository repository,
            IErrorHandler error,
            IValidator<CoinTradeInputDto> validator) :
            base(mapper, repository, error, validator)
        {
        }

        public void AddCoinTrade(CoinTradeInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Coin.CoinTrade.CoinTrade>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<CoinTradeOutputDto>> GetListCoinTrade(CoinTradeInputDto input)
        {
            return GetPage(input);
        }
    }
}