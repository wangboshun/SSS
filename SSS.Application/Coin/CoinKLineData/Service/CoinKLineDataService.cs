using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Coin.CoinKLineData.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Coin.CoinKLineData;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Coin.CoinKLineData.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(ICoinKLineDataService))]
    public class CoinKLineDataService : QueryService<SSS.Domain.Coin.CoinKLineData.CoinKLineData, CoinKLineDataInputDto, CoinKLineDataOutputDto>, ICoinKLineDataService
    {
        public CoinKLineDataService(IMapper mapper,
            ICoinKLineDataRepository repository,
            IErrorHandler error,
            IValidator<CoinKLineDataInputDto> validator) :
            base(mapper, repository, error, validator)
        {

        }

        public CoinKLineDataOutputDto AddCoinKLineData(CoinKLineDataInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return null;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Coin.CoinKLineData.CoinKLineData>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            return Repository.SaveChanges() > 0 ? Mapper.Map<CoinKLineDataOutputDto>(model) : null;
        }

        public Pages<List<CoinKLineDataOutputDto>> GetListCoinKLineData(CoinKLineDataInputDto input)
        {
            return GetPage(input);
        } 
    }
}