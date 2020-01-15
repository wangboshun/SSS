using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Domain.Permission.Info.UserInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Group.UserGroup.Service
{
    public interface IUserGroupService : IQueryService<Domain.Permission.Group.UserGroup.UserGroup, UserGroupInputDto, UserGroupOutputDto>
    {
        UserGroupOutputDto AddUserGroup(UserGroupInputDto input);

        bool DeleteUserGroup(string id);

        Pages<List<UserGroupOutputDto>> GetListUserGroup(UserGroupInputDto input);

        /// <summary>
        /// ����Ȩ����Id�����ƣ����������û���
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserGroupOutputDto>> GetUserGroupByPowerGroup(PowerGroupInputDto input);

        /// <summary>
        /// ���ݽ�ɫ��Id�����ƣ����������û���
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserGroupOutputDto>> GetUserGroupByRoleGroup(RoleGroupInputDto input);

        /// <summary>
        /// �����û�Id�����ƣ����������û���
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserGroupOutputDto>> GetUserGroupByUser(UserInfoInputDto input);
    }
}