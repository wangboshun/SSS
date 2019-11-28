using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.RoleMenu.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.RoleMenu;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.RoleMenu.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleMenuService))]
    public class RoleMenuService : QueryService<SSS.Domain.Permission.RoleMenu.RoleMenu, RoleMenuInputDto, RoleMenuOutputDto>, IRoleMenuService
    {
        private readonly IRoleMenuRepository _repository;
        public RoleMenuService(IMapper mapper,
            IRoleMenuRepository repository,
            IErrorHandler error,
            IValidator<RoleMenuInputDto> validator) :
            base(mapper, repository, error, validator)
        {
            _repository = repository;
        }

        public void AddRoleMenu(RoleMenuInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Permission.RoleMenu.RoleMenu>(input);
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<RoleMenuOutputDto>> GetListRoleMenu(RoleMenuInputDto input)
        {
            return GetPage(input);
        }

        public List<RoleMenuOutputDto> GetMenuByRole(string roleid)
        {
            return _repository.GetMenuByRole(roleid);
        }
    }
}