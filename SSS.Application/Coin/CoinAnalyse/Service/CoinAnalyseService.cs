using AutoMapper;
using AutoMapper.QueryableExtensions;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Coin.CoinAnalyse.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Coin.CoinAnalyse;
using SSS.Infrastructure.Repository.Coin.CoinInfo;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Application.Coin.CoinAnalyse.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(ICoinAnalyseService))]
    public class CoinAnalyseService :
        QueryService<Domain.Coin.CoinAnalyse.CoinAnalyse, CoinAnalyseInputDto, CoinAnalyseOutputDto>,
        ICoinAnalyseService
    {
        private readonly ICoinInfoRepository _coininforepository;
        private readonly ICoinAnalyseRepository _repository;

        public CoinAnalyseService(IMapper mapper,
            ICoinAnalyseRepository repository,
            IErrorHandler error,
            IValidator<CoinAnalyseInputDto> validator,
            ICoinInfoRepository coininforepository) :
            base(mapper, repository, error, validator)
        {
            _coininforepository = coininforepository;
            _repository = repository;
        }

        public void AddCoinAnalyse(CoinAnalyseInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Coin.CoinAnalyse.CoinAnalyse>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<CoinAnalyseOutputDto>> GetListCoinAnalyse(CoinAnalyseInputDto input)
        {
            int count = 0;
            var data = _repository.GetPageOrderByAsc(input, ref count);
            var list = data.ProjectTo<CoinAnalyseOutputDto>(Mapper.ConfigurationProvider).ToList();

            foreach (var item in list)
                item.Logo = _coininforepository.Get(x => x.Coin.Equals(item.Coin.Replace("-USDT", "")))?.RomteLogo;

            return new Pages<List<CoinAnalyseOutputDto>>(list, count);
        }
    }
}