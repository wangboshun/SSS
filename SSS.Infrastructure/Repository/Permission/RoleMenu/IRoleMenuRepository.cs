using SSS.Domain.Permission.RoleMenu.Dto;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.RoleMenu
{
    public interface IRoleMenuRepository : IRepository<SSS.Domain.Permission.RoleMenu.RoleMenu>
    {
        /// <summary>
        /// 获取角色下的所有菜单
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        List<RoleMenuOutputDto> GetRoleMenuByRole(string roleid);

        /// <summary>
        /// 删除角色下的所有菜单
        /// </summary>
        /// <param name="roleid"></param>
        bool DeleteRoleMenuByRole(string roleid);
    }
}