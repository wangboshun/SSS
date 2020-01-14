using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Coin.CoinInfo.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Coin.CoinInfo;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Coin.CoinInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(ICoinInfoService))]
    public class CoinInfoService : QueryService<Domain.Coin.CoinInfo.CoinInfo, CoinInfoInputDto, CoinInfoOutputDto>,
        ICoinInfoService
    {
        public CoinInfoService(IMapper mapper,
            ICoinInfoRepository repository,
            IErrorHandler error,
            IValidator<CoinInfoInputDto> validator) :
            base(mapper, repository, error, validator)
        {
        }

        public CoinInfoOutputDto AddCoinInfo(CoinInfoInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return null;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Coin.CoinInfo.CoinInfo>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            return Repository.SaveChanges() > 0 ? Mapper.Map<CoinInfoOutputDto>(model) : null;
        }

        public Pages<List<CoinInfoOutputDto>> GetListCoinInfo(CoinInfoInputDto input)
        {
            return GetPage(input);
        }
    }
}