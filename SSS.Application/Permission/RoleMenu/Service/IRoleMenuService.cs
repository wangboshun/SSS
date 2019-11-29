using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.RoleMenu.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.RoleMenu.Service
{
    public interface IRoleMenuService : IQueryService<SSS.Domain.Permission.RoleMenu.RoleMenu, RoleMenuInputDto, RoleMenuOutputDto>
    {
        void AddRoleMenu(RoleMenuInputDto input);

        /// <summary>
        /// 删除角色下的所有菜单
        /// </summary>
        /// <param name="roleid"></param>
        bool DeleteRoleMenuByRole(string roleid);

        Pages<List<RoleMenuOutputDto>> GetListRoleMenu(RoleMenuInputDto input);

        /// <summary>
        /// 获取角色下所有菜单
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        List<RoleMenuOutputDto> GetMenuByRole(string roleid);
    }
}