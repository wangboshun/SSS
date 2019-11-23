using AutoMapper;
using AutoMapper.QueryableExtensions;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.DigitalCurrency.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.CoinInfo;
using SSS.Infrastructure.Repository.DigitalCurrency;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Application.DigitalCurrency.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IDigitalCurrencyService))]
    public class DigitalCurrencyService :
        QueryService<Domain.DigitalCurrency.DigitalCurrency, DigitalCurrencyInputDto, DigitalCurrencyOutputDto>,
        IDigitalCurrencyService
    {
        private readonly ICoinInfoRepository _coininforepository;
        private readonly IDigitalCurrencyRepository _repository;

        public DigitalCurrencyService(IMapper mapper,
            IDigitalCurrencyRepository repository,
            IErrorHandler error,
            IValidator<DigitalCurrencyInputDto> validator,
            ICoinInfoRepository coininforepository) :
            base(mapper, repository, error, validator)
        {
            _coininforepository = coininforepository;
            _repository = repository;
        }

        public void AddDigitalCurrency(DigitalCurrencyInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.DigitalCurrency.DigitalCurrency>(input);
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<DigitalCurrencyOutputDto>> GetListDigitalCurrency(DigitalCurrencyInputDto input)
        {
            int count = 0;
            var data = _repository.GetPageOrderByAsc(input, ref count);
            var list = data.ProjectTo<DigitalCurrencyOutputDto>(Mapper.ConfigurationProvider).ToList();

            foreach (var item in list)
                item.Logo = _coininforepository.Get(x => x.Coin.Equals(item.Coin.Replace("-USDT", "")))?.RomteLogo;

            return new Pages<List<DigitalCurrencyOutputDto>>(list, count);
        }
    }
}