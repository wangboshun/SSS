using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Coin.CoinMessage.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Coin.CoinInfo;
using SSS.Infrastructure.Repository.Coin.CoinMessage;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Mapper;

using System;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Application.Coin.CoinMessage.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(ICoinMessageService))]
    public class CoinMessageService :
        QueryService<Domain.Coin.CoinMessage.CoinMessage, CoinMessageInputDto, CoinMessageOutputDto>,
        ICoinMessageService
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

        public CoinMessageOutputDto AddCoinMessage(CoinMessageInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return null;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Coin.CoinMessage.CoinMessage>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            return Repository.SaveChanges() > 0 ? Mapper.Map<CoinMessageOutputDto>(model) : null;
        }

        public Pages<List<CoinMessageOutputDto>> GetListCoinMessage(CoinMessageInputDto input)
        {
            int count = 0;
            var data = _coinmessagerepository.GetPageOrderByAsc(input, ref count);
            var list = data.MapperToOutPut<CoinMessageOutputDto>().ToList();
            foreach (var item in list) item.Logo = _coininforepository.Get(x => x.Coin.Equals(item.Coin))?.RomteLogo;

            return new Pages<List<CoinMessageOutputDto>>(list, count);
        }
    }
}