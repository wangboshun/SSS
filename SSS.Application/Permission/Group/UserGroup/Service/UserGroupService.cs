using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Domain.Permission.Relation.UserGroupRelation.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.UserGroup;
using SSS.Infrastructure.Repository.Permission.Relation.UserGroupRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.Group.UserGroup.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserGroupService))]
    public class UserGroupService :
        QueryService<Domain.Permission.Group.UserGroup.UserGroup, UserGroupInputDto, UserGroupOutputDto>,
        IUserGroupService
    {
        private readonly IUserGroupRelationRepository _userGroupRelationRepository;
        public UserGroupService(IMapper mapper,
            IUserGroupRepository repository,
            IErrorHandler error,
            IValidator<UserGroupInputDto> validator,
            IUserGroupRelationRepository userGroupRelationRepository) :
            base(mapper, repository, error, validator)
        {
            _userGroupRelationRepository = userGroupRelationRepository;
        }

        public void AddUserGroup(UserGroupInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Group.UserGroup.UserGroup>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<UserGroupOutputDto>> GetListUserGroup(UserGroupInputDto input)
        {
            return GetPage(input);
        }

        public void DeleteUserGroup(UserGroupInputDto input)
        {
            Repository.Remove(input.id);
        }

        /// <summary>
        /// 根据用户Id或名称，遍历关联用户组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<UserGroupRelationOutputDto>> GetUserGroupByUser(UserGroupRelationInputDto input)
        {
            return _userGroupRelationRepository.GetUserGroupByUser(input);
        }
    }
}