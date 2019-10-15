using AutoMapper;
using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Application.Seedwork.Service;
using SSS.Domain.Articel.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Articel;
using SSS.Infrastructure.Util.Attribute;
using System;
using System.Collections.Generic;

namespace SSS.Application.Articel.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IArticelService))]
    public class ArticelService : QueryService<SSS.Domain.Articel.Articel, ArticelInputDto, ArticelOutputDto>, IArticelService
    {
        public ArticelService(IMapper mapper,
            IArticelRepository repository,
            IErrorHandler error,
            IValidator<ArticelInputDto> validator) :
            base(mapper, repository, error, validator)
        {
        }

        public void AddArticel(ArticelInputDto input)
        {
            var result = _validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                _error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = _mapper.Map<SSS.Domain.Articel.Articel>(input);
            _repository.Add(model);
            _repository.SaveChanges();
        }

        public Pages<List<ArticelOutputDto>> GetListArticel(ArticelInputDto input)
        {
            return GetList(input);
        }
    }
}