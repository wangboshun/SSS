using AutoMapper;
using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Application.Seedwork.Service;
using SSS.Domain.DigitalCurrency.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.DigitalCurrency;
using SSS.Infrastructure.Util.Attribute;
using System;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Application.DigitalCurrency.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IDigitalCurrencyService))]
    public class DigitalCurrencyService : QueryService<SSS.Domain.DigitalCurrency.DigitalCurrency, DigitalCurrencyInputDto, DigitalCurrencyOutputDto>, IDigitalCurrencyService
    {
        public DigitalCurrencyService(IMapper mapper,
            IDigitalCurrencyRepository repository,
            IErrorHandler error,
            IValidator<DigitalCurrencyInputDto> validator) :
            base(mapper, repository, error, validator)
        {

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
            var model = Mapper.Map<SSS.Domain.DigitalCurrency.DigitalCurrency>(input);
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<DigitalCurrencyOutputDto>> GetListDigitalCurrency(DigitalCurrencyInputDto input)
        {
            var data = GetList(x => x.IsDelete == 0);
            data.data = data.data.OrderByDescending(x => x.CloseRange).ToList();
            return data;
        }
    }
}