using System;
using System.Collections.Generic;
using System.Linq;
using AutoMapper;
using AutoMapper.QueryableExtensions;
using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Application.Seedwork.Service;
using SSS.Domain.CoinMessage.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.CoinInfo;
using SSS.Infrastructure.Repository.CoinMessage;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Application.CoinMessage.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(ICoinMessageService))]
    public class CoinMessageService : QueryService<Domain.CoinMessage.CoinMessage, CoinMessageInputDto, CoinMessageOutputDto>, ICoinMessageService
    {
        private readonly ICoinInfoRepository _coininforepository;
        private readonly ICoinMessageRepository _coinmessagerepository;

        public CoinMessageService(IMapper mapper,
            ICoinMessageRepository repository,
            IErrorHandler error,
            IValidator<CoinMessageInputDto> validator,
            ICoinInfoRepository coininforepository) :
            base(mapper, repository, error, validator)
        {
            _coininforepository = coininforepository;
            _coinmessagerepository = repository;
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
            var model = Mapper.Map<Domain.CoinMessage.CoinMessage>(input);
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<CoinMessageOutputDto>> GetListCoinMessage(CoinMessageInputDto input)
        {
            int count = 0;
            var data = _coinmessagerepository.GetPageOrderByAsc(input, ref count);
            var list = data.ProjectTo<CoinMessageOutputDto>(Mapper.ConfigurationProvider).ToList();
            foreach (var item in list) item.Logo = _coininforepository.Get(x => x.Coin.Equals(item.Coin))?.RomteLogo;

            return new Pages<List<CoinMessageOutputDto>>(list, count);
        }
    }
}