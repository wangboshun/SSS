using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Domain.Permission.Relation.UserGroupPowerGroupRelation.Dto;
using SSS.Domain.Permission.Relation.UserGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Group.UserGroup.Service
{
    public interface IUserGroupService : IQueryService<Domain.Permission.Group.UserGroup.UserGroup, UserGroupInputDto,
        UserGroupOutputDto>
    {
        void AddUserGroup(UserGroupInputDto input);

        Pages<List<UserGroupOutputDto>> GetListUserGroup(UserGroupInputDto input);

        void DeleteUserGroup(UserGroupInputDto input);

        /// <summary>
        ///  根据用户Id或名称，遍历关联用户组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserGroupRelationOutputDto>> GetUserGroupByUser(UserGroupRelationInputDto input);

        /// <summary>
        /// 根据权限组Id或名称，遍历关联用户组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserGroupPowerGroupRelationOutputDto>> GetUserGroupByPowerGroup(UserGroupPowerGroupRelationInputDto input);
    }
}