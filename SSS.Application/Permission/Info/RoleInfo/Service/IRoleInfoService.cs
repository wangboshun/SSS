using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Info.RoleInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;
using SSS.Domain.Permission.Group.PowerGroup.Dto;

namespace SSS.Application.Permission.Info.RoleInfo.Service
{
    public interface IRoleInfoService : IQueryService<Domain.Permission.Info.RoleInfo.RoleInfo, RoleInfoInputDto,
        RoleInfoOutputDto>
    {
        bool AddRoleInfo(RoleInfoInputDto input);
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
    }
}