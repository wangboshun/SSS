using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Domain.Permission.Info.RoleInfo.Dto;
using SSS.Domain.Permission.Info.UserInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Group.RoleGroup.Service
{
    public interface IRoleGroupService : IQueryService<Domain.Permission.Group.RoleGroup.RoleGroup, RoleGroupInputDto, RoleGroupOutputDto>
    {
        RoleGroupOutputDto AddRoleGroup(RoleGroupInputDto input);

        Pages<List<RoleGroupOutputDto>> GetListRoleGroup(RoleGroupInputDto input);

        bool DeleteRoleGroup(string id);

        /// <summary>
        ///     ���ݽ�ɫId�����ƣ�����������ɫ��
        /// </summary>
        /// <param name="input"></param>
        Pages<List<RoleGroupOutputDto>> GetRoleGroupByRole(RoleInfoInputDto input);

        /// <summary>
        ///     ����Ȩ����Id�����ƣ�����������ɫ��
        /// </summary>
        /// <param name="input"></param>
        Pages<List<RoleGroupOutputDto>> GetRoleGroupByPowerGroup(PowerGroupInputDto input);

        /// <summary>
        ///     �����û���Id�����ƣ�����������ɫ��
        /// </summary>
        /// <param name="input"></param>
        Pages<List<RoleGroupOutputDto>> GetRoleGroupByUserGroup(UserGroupInputDto input);

        /// <summary>
        ///     �����û�Id�����ƣ�����������ɫ��
        /// </summary>
        /// <param name="input"></param>
        Pages<List<RoleGroupOutputDto>> GetRoleGroupByUser(UserInfoInputDto input);
    }
}