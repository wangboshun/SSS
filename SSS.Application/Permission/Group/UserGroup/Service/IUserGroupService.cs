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
        ///  �����û�Id�����ƣ����������û���
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserGroupRelationOutputDto>> GetUserGroupByUser(UserGroupRelationInputDto input);

        /// <summary>
        /// ����Ȩ����Id�����ƣ����������û���
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserGroupPowerGroupRelationOutputDto>> GetUserGroupByPowerGroup(UserGroupPowerGroupRelationInputDto input);
    }
}