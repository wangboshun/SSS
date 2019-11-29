using SSS.Domain.Permission.RoleMenu.Dto;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.RoleMenu
{
    public interface IRoleMenuRepository : IRepository<SSS.Domain.Permission.RoleMenu.RoleMenu>
    {
        /// <summary>
        /// ��ȡ��ɫ�µ����в˵�
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        List<RoleMenuOutputDto> GetRoleMenuByRole(string roleid);

        /// <summary>
        /// ɾ����ɫ�µ����в˵�
        /// </summary>
        /// <param name="roleid"></param>
        bool DeleteRoleMenuByRole(string roleid);
    }
}