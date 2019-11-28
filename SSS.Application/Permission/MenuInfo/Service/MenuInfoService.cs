using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.MenuInfo.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.MenuInfo;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.MenuInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IMenuInfoService))]
    public class MenuInfoService : QueryService<SSS.Domain.Permission.MenuInfo.MenuInfo, MenuInfoInputDto, MenuInfoOutputDto>, IMenuInfoService
    {
        private readonly IMenuInfoRepository _repository;

        public MenuInfoService(IMapper mapper,
            IMenuInfoRepository repository,
            IErrorHandler error,
            IValidator<MenuInfoInputDto> validator) :
            base(mapper, repository, error, validator)
        {
            _repository = repository;
        }

        public void AddMenuInfo(MenuInfoInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Permission.MenuInfo.MenuInfo>(input);
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public List<MenuInfoTreeOutputDto> GetChildren(MenuInfoInputDto input)
        {
            return _repository.GetChildren(input);
        }

        public Pages<List<MenuInfoOutputDto>> GetListMenuInfo(MenuInfoInputDto input)
        {
            return GetPage(input);
        }
    }
}