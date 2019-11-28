using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.RoleMenu.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.RoleMenu.Service
{
    public interface IRoleMenuService : IQueryService<SSS.Domain.Permission.RoleMenu.RoleMenu, RoleMenuInputDto, RoleMenuOutputDto>
    {
        void AddRoleMenu(RoleMenuInputDto input);

        Pages<List<RoleMenuOutputDto>> GetListRoleMenu(RoleMenuInputDto input);

        List<RoleMenuOutputDto> GetMenuByRole(string roleid);
    }
}