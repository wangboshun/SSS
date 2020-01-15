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
        ///     根据角色Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="input"></param>
        Pages<List<RoleGroupOutputDto>> GetRoleGroupByRole(RoleInfoInputDto input);

        /// <summary>
        ///     根据权限组Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="input"></param>
        Pages<List<RoleGroupOutputDto>> GetRoleGroupByPowerGroup(PowerGroupInputDto input);

        /// <summary>
        ///     根据用户组Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="input"></param>
        Pages<List<RoleGroupOutputDto>> GetRoleGroupByUserGroup(UserGroupInputDto input);

        /// <summary>
        ///     根据用户Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="input"></param>
        Pages<List<RoleGroupOutputDto>> GetRoleGroupByUser(UserInfoInputDto input);
    }
}