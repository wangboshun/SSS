using SSS.Domain.Permission.RoleMenu.Dto;
using SSS.Domain.Seedwork.Repository;
using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.RoleMenu
{
    public interface IRoleMenuRepository : IRepository<SSS.Domain.Permission.RoleMenu.RoleMenu>
    {
        List<RoleMenuOutputDto> GetMenuByRole(string roleid);
    }
}