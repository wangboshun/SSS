using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Relation.RoleGroupRelation.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.RoleGroup;
using SSS.Infrastructure.Repository.Permission.Relation.RoleGroupRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.Group.RoleGroup.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleGroupService))]
    public class RoleGroupService :
        QueryService<Domain.Permission.Group.RoleGroup.RoleGroup, RoleGroupInputDto, RoleGroupOutputDto>,
        IRoleGroupService
    {
        private readonly IRoleGroupRelationRepository _roleGroupRelationRepository;

        public RoleGroupService(IMapper mapper,
            IRoleGroupRepository repository,
            IErrorHandler error,
            IValidator<RoleGroupInputDto> validator,
            IRoleGroupRelationRepository roleGroupRelationRepository) :
            base(mapper, repository, error, validator)
        {
            _roleGroupRelationRepository = roleGroupRelationRepository;
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
        public Pages<List<RoleGroupRelationOutputDto>> GetRoleGroupByRole(RoleGroupRelationInputDto input)
        {
            return _roleGroupRelationRepository.GetRoleGroupByRole(input.roleid, input.rolename, input.parentid, input.pageindex, input.pagesize);
        }
    }
}