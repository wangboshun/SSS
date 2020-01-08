using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Domain.Permission.Info.UserInfo.Dto;
using SSS.Domain.Permission.Relation.UserGroupRoleGroupRelation;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.RoleGroup;
using SSS.Infrastructure.Repository.Permission.Group.UserGroup;
using SSS.Infrastructure.Repository.Permission.Relation.UserGroupRoleGroupRelation;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Mapper;

using System;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Application.Permission.Group.UserGroup.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserGroupService))]
    public class UserGroupService :
        QueryService<Domain.Permission.Group.UserGroup.UserGroup, UserGroupInputDto, UserGroupOutputDto>,
        IUserGroupService
    {
        private readonly IUserGroupRepository _userGroupRepository;
        private readonly IRoleGroupRepository _roleGroupRepository;
        private readonly IUserGroupRoleGroupRelationRepository _userGroupRoleGroupRelationRepository;

        public UserGroupService(IMapper mapper,
            IUserGroupRepository repository,
            IErrorHandler error,
            IValidator<UserGroupInputDto> validator,
            IRoleGroupRepository roleGroupRepository,
            IUserGroupRoleGroupRelationRepository userGroupRoleGroupRelationRepository) :
            base(mapper, repository, error, validator)
        {
            _userGroupRepository = repository;
            _roleGroupRepository = roleGroupRepository;
            _userGroupRoleGroupRelationRepository = userGroupRoleGroupRelationRepository;
        }

        public UserGroupOutputDto AddUserGroup(UserGroupInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return null;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Group.UserGroup.UserGroup>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);

            if (!string.IsNullOrWhiteSpace(input.rolegroupid))
            {
                var rolegroup = _roleGroupRepository.Get(input.rolegroupid);

                if (rolegroup != null)
                {
                    _userGroupRoleGroupRelationRepository.Add(new UserGroupRoleGroupRelation()
                    {
                        CreateTime = DateTime.Now,
                        Id = Guid.NewGuid().ToString(),
                        IsDelete = 0,
                        RoleGroupId = rolegroup.Id,
                        UserGroupId = model.Id
                    });
                }
            }

            return Repository.SaveChanges() > 0 ? Mapper.Map<UserGroupOutputDto>(model) : null;
        }

        public Pages<List<UserGroupOutputDto>> GetListUserGroup(UserGroupInputDto input)
        {
            return GetPage(input);
        }

        public bool DeleteUserGroup(string id)
        {
            return Repository.Remove(id, true);
        }

        /// <summary>
        /// 根据用户Id或名称，遍历关联用户组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<UserGroupOutputDto>> GetUserGroupByUser(UserInfoInputDto input)
        {
            var data = _userGroupRepository.GetUserGroupByUser(input.id, input.username, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<UserGroupOutputDto>>(data.items.MapperToOutPut<UserGroupOutputDto>()?.ToList(), data.count);

        }

        public Pages<List<UserGroupOutputDto>> GetUserGroupByPowerGroup(PowerGroupInputDto input)
        {
            var data = _userGroupRepository.GetUserGroupByPowerGroup(input.id, input.powergroupname, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<UserGroupOutputDto>>(data.items.MapperToOutPut<UserGroupOutputDto>()?.ToList(), data.count);
        }

        /// <summary>
        /// 根据角色组Id或名称，遍历关联用户组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<UserGroupOutputDto>> GetUserGroupByRoleGroup(RoleGroupInputDto input)
        {
            var data = _userGroupRepository.GetUserGroupByRoleGroup(input.id, input.rolegroupname, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<UserGroupOutputDto>>(data.items.MapperToOutPut<UserGroupOutputDto>()?.ToList(), data.count);
        }
    }
}