using AutoMapper;
using AutoMapper.QueryableExtensions;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Info.RoleInfo.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.RoleGroup;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Application.Permission.Group.RoleGroup.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleGroupService))]
    public class RoleGroupService :
        QueryService<Domain.Permission.Group.RoleGroup.RoleGroup, RoleGroupInputDto, RoleGroupOutputDto>,
        IRoleGroupService
    {
        private readonly IRoleGroupRepository _roleGroupRepository;

        public RoleGroupService(IMapper mapper,
            IRoleGroupRepository repository,
            IErrorHandler error,
            IValidator<RoleGroupInputDto> validator) :
            base(mapper, repository, error, validator)
        {
            _roleGroupRepository = repository;
        }

        public void AddRoleGroup(RoleGroupInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Group.RoleGroup.RoleGroup>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<RoleGroupOutputDto>> GetListRoleGroup(RoleGroupInputDto input)
        {
            return GetPage(input);
        }

        public void DeleteRoleGroup(RoleGroupInputDto input)
        {
            Repository.Remove(input.id);
        }

        /// <summary>
        /// 根据权限Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<RoleGroupOutputDto>> GetRoleGroupByRole(RoleInfoInputDto input)
        {
            var data = _roleGroupRepository.GetRoleGroupByRole(input.id, input.rolename, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<RoleGroupOutputDto>>(data.items.AsQueryable().ProjectTo<RoleGroupOutputDto>(Mapper.ConfigurationProvider).ToList(), data.count);
        }
    }
}