using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Domain.Permission.Info.RoleInfo.Dto;
using SSS.Domain.Permission.Info.UserInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Info.RoleInfo.Service
{
    public interface IRoleInfoService : IQueryService<Domain.Permission.Info.RoleInfo.RoleInfo, RoleInfoInputDto, RoleInfoOutputDto>
    {
        RoleInfoOutputDto AddRoleInfo(RoleInfoInputDto input);
        Pages<List<RoleInfoOutputDto>> GetListRoleInfo(RoleInfoInputDto input);

        /// <summary>
        ///     获取角色下的所有下级
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        List<RoleInfoTreeOutputDto> GetChildren(string roleid);

        bool DeleteRoleInfo(string id);

        /// <summary>
        /// 根据角色组Id或名称，遍历关联角色
        /// </summary>
        /// <param name="input"></param>
        Pages<List<RoleInfoOutputDto>> GetRoleByRoleGroup(RoleGroupInputDto input);

        /// <summary>
        /// 根据权限组Id或名称，遍历关联角色
        /// </summary>
        /// <param name="input"></param>
        Pages<List<RoleInfoOutputDto>> GetRoleByPowerGroup(PowerGroupInputDto input);

        /// <summary>
        /// 根据用户组Id或名称，遍历关联角色
        /// </summary>
        /// <param name="input"></param>
        Pages<List<RoleInfoOutputDto>> GetRoleByUserGroup(UserGroupInputDto input);

        /// <summary>
        /// 根据用户Id或名称，遍历关联角色
        /// </summary>
        /// <param name="input"></param>
        Pages<List<RoleInfoOutputDto>> GetRoleByUser(UserInfoInputDto input);
    }
}