using AutoMapper;
using AutoMapper.QueryableExtensions;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Coin.CoinArticel.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.CoinArticel;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Application.Coin.CoinArticel.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(ICoinArticelService))]
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

        public bool AddCoinArticel(CoinArticelInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return false;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Coin.CoinArticel.CoinArticel>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            return Repository.SaveChanges()>0;
        }

        public Pages<List<CoinArticelOutputDto>> GetListCoinArticel(CoinArticelInputDto input)
        {
            return GetPage(input);
        }

        public List<CoinArticelOutputDto> GetNews(CoinArticelInputDto input)
        {
            var data = _repository.GetNews(input);
            return data?.AsQueryable().ProjectTo<CoinArticelOutputDto>(Mapper.ConfigurationProvider).ToList();
        }
    }
}