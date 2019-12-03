using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.RoleGroup;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using SSS.Domain.Permission.Relation.RoleRoleGroupRelation.Dto;
using SSS.Infrastructure.Repository.Permission.Relation.RoleRoleGroupRelation;

namespace SSS.Application.Permission.Group.RoleGroup.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleGroupService))]
    public class RoleGroupService :
        QueryService<Domain.Permission.Group.RoleGroup.RoleGroup, RoleGroupInputDto, RoleGroupOutputDto>,
        IRoleGroupService
    {
        private readonly IRoleUserGroupRelationRepository _roleRoleGroupRelationRepository;

        public RoleGroupService(IMapper mapper,
            IRoleGroupRepository repository,
            IErrorHandler error,
            IValidator<RoleGroupInputDto> validator,
            IRoleUserGroupRelationRepository roleRoleGroupRelationRepository) :
            base(mapper, repository, error, validator)
        {
            _roleRoleGroupRelationRepository = roleRoleGroupRelationRepository;
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
            Delete(input.id);
        }

        /// <summary>
        /// ����Ȩ��Id�����ƣ�����������ɫ��
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<RoleRoleGroupRelationOutputDto>> GetRoleGroupByRole(RoleRoleGroupRelationInputDto input)
        {
            return _roleRoleGroupRelationRepository.GetRoleGroupByRole(input);
        }
    }
}