using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Domain.Permission.Info.RoleInfo.Dto;
using SSS.Domain.Permission.Info.UserInfo.Dto;
using SSS.Domain.Permission.Relation.RoleGroupPowerGroupRelation;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.PowerGroup;
using SSS.Infrastructure.Repository.Permission.Group.RoleGroup;
using SSS.Infrastructure.Repository.Permission.Relation.RoleGroupPowerGroupRelation;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Mapper;

using System;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Application.Permission.Group.RoleGroup.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleGroupService))]
    public class RoleGroupService : QueryService<Domain.Permission.Group.RoleGroup.RoleGroup, RoleGroupInputDto, RoleGroupOutputDto>, IRoleGroupService
    {
        private readonly IPowerGroupRepository _powerGroupRepository;
        private readonly IRoleGroupPowerGroupRelationRepository _roleGroupPowerGroupRelationRepository;
        private readonly IRoleGroupRepository _roleGroupRepository;

        public RoleGroupService(IMapper mapper,
            IRoleGroupRepository repository,
            IErrorHandler error,
            IValidator<RoleGroupInputDto> validator,
            IPowerGroupRepository powerGroupRepository,
            IRoleGroupPowerGroupRelationRepository roleGroupPowerGroupRelationRepository) :
            base(mapper, repository, error, validator)
        {
            _roleGroupRepository = repository;
            _powerGroupRepository = powerGroupRepository;
            _roleGroupPowerGroupRelationRepository = roleGroupPowerGroupRelationRepository;
        }

        public RoleGroupOutputDto AddRoleGroup(RoleGroupInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return null;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Group.RoleGroup.RoleGroup>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);

            if (!string.IsNullOrWhiteSpace(input.powergroupid))
            {
                var powergroup = _powerGroupRepository.Get(input.powergroupid);
                if (powergroup != null)
                    _roleGroupPowerGroupRelationRepository.Add(new RoleGroupPowerGroupRelation
                    {
                        CreateTime = DateTime.Now,
                        Id = Guid.NewGuid().ToString(),
                        IsDelete = 0,
                        PowerGroupId = powergroup.Id,
                        RoleGroupId = model.Id
                    });
            }

            return Repository.SaveChanges() > 0 ? Mapper.Map<RoleGroupOutputDto>(model) : null;
        }

        public Pages<List<RoleGroupOutputDto>> GetListRoleGroup(RoleGroupInputDto input)
        {
            return GetPage(input);
        }

        public bool DeleteRoleGroup(string id)
        {
            return Repository.Remove(id, true);
        }

        /// <summary>
        ///     根据权限Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<RoleGroupOutputDto>> GetRoleGroupByRole(RoleInfoInputDto input)
        {
            var data = _roleGroupRepository.GetRoleGroupByRole(input.id, input.rolename, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<RoleGroupOutputDto>>(data.items.MapperToOutPut<RoleGroupOutputDto>()?.ToList(), data.count);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<RoleGroupOutputDto>> GetRoleGroupByPowerGroup(PowerGroupInputDto input)
        {
            var data = _roleGroupRepository.GetRoleGroupByPowerGroup(input.id, input.powergroupname, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<RoleGroupOutputDto>>(data.items.MapperToOutPut<RoleGroupOutputDto>()?.ToList(), data.count);
        }

        /// <summary>
        /// 根据用户组Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<RoleGroupOutputDto>> GetRoleGroupByUserGroup(UserGroupInputDto input)
        {
            var data = _roleGroupRepository.GetRoleGroupByUserGroup(input.id, input.usergroupname, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<RoleGroupOutputDto>>(data.items.MapperToOutPut<RoleGroupOutputDto>()?.ToList(), data.count);
        }

        /// <summary>
        /// 根据用户Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<RoleGroupOutputDto>> GetRoleGroupByUser(UserInfoInputDto input)
        {
            var data = _roleGroupRepository.GetRoleGroupByUser(input.id, input.username, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<RoleGroupOutputDto>>(data.items.MapperToOutPut<RoleGroupOutputDto>()?.ToList(), data.count);
        }
    }
}