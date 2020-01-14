using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Coin.CoinArticel.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.CoinArticel;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Mapper;

using System;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Application.Coin.CoinArticel.Service
{
    [DIService(ServiceLifetime.Singleton, typeof(ICoinArticelService))]
    public class CoinArticelService :
        QueryService<Domain.Coin.CoinArticel.CoinArticel, CoinArticelInputDto, CoinArticelOutputDto>,
        ICoinArticelService
    {
        private readonly ICoinArticelRepository _repository;

        public CoinArticelService(IMapper mapper,
            ICoinArticelRepository repository,
            IErrorHandler error,
            IValidator<CoinArticelInputDto> validator) :
            base(mapper, repository, error, validator)
        {
            _repository = repository;
        }

        public CoinArticelOutputDto AddCoinArticel(CoinArticelInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return null;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Coin.CoinArticel.CoinArticel>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            return Repository.SaveChanges() > 0 ? Mapper.Map<CoinArticelOutputDto>(model) : null;
        }

        public Pages<List<CoinArticelOutputDto>> GetListCoinArticel(CoinArticelInputDto input)
        {
            return GetPage(input);
        }

        public List<CoinArticelOutputDto> GetNews(CoinArticelInputDto input)
        {
            var data = _repository.GetNews(input);
            return data.MapperToOutPut<CoinArticelOutputDto>()?.ToList();
        }
    }
}