using System;
using System.Collections.Generic;
using AutoMapper;
using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Application.Seedwork.Service;
using SSS.Domain.CoinInfo.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.CoinInfo;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Application.CoinInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(ICoinInfoService))]
    public class CoinInfoService : QueryService<Domain.CoinInfo.CoinInfo, CoinInfoInputDto, CoinInfoOutputDto>,ICoinInfoService
    {
        public CoinInfoService(IMapper mapper,
            ICoinInfoRepository repository,
            IErrorHandler error,
            IValidator<CoinInfoInputDto> validator) :
            base(mapper, repository, error, validator)
        {
        }

        public void AddCoinInfo(CoinInfoInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.CoinInfo.CoinInfo>(input);
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<CoinInfoOutputDto>> GetListCoinInfo(CoinInfoInputDto input)
        {
            return GetPage(input);
        }
    }
}