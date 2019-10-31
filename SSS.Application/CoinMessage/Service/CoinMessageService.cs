using AutoMapper;
using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Application.Seedwork.Service;
using SSS.Domain.CoinMessage.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.CoinMessage;
using SSS.Infrastructure.Util.Attribute;
using System;
using System.Collections.Generic;

namespace SSS.Application.CoinMessage.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(ICoinMessageService))]
    public class CoinMessageService : QueryService<SSS.Domain.CoinMessage.CoinMessage, CoinMessageInputDto, CoinMessageOutputDto>, ICoinMessageService
    {
        public CoinMessageService(IMapper mapper,
            ICoinMessageRepository repository,
            IErrorHandler error,
            IValidator<CoinMessageInputDto> validator) :
            base(mapper, repository, error, validator)
        {

        }

        public void AddCoinMessage(CoinMessageInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.CoinMessage.CoinMessage>(input);
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<CoinMessageOutputDto>> GetListCoinMessage(CoinMessageInputDto input)
        {
            return GetPage(input);
        }
    }
}