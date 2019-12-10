using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Info.RoleInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;
using SSS.Domain.Permission.Group.PowerGroup.Dto;

namespace SSS.Application.Permission.Group.RoleGroup.Service
{
    public interface IRoleGroupService : IQueryService<Domain.Permission.Group.RoleGroup.RoleGroup, RoleGroupInputDto,
        RoleGroupOutputDto>
    {
        bool AddRoleGroup(RoleGroupInputDto input);

        Pages<List<RoleGroupOutputDto>> GetListRoleGroup(RoleGroupInputDto input);

        bool DeleteRoleGroup(string id);

        /// <summary>
        /// 根据角色Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="input"></param>
        Pages<List<RoleGroupOutputDto>> GetRoleGroupByRole(RoleInfoInputDto input);

        /// <summary>
        /// 根据权限组Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="input"></param>
        Pages<List<RoleGroupOutputDto>> GetRoleGroupByPowerGroup(PowerGroupInputDto input);
        
    }
}