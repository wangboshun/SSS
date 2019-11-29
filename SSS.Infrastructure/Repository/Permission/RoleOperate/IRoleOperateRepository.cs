using SSS.Domain.Permission.RoleOperate.Dto;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.RoleOperate
{
    public interface IRoleOperateRepository : IRepository<SSS.Domain.Permission.RoleOperate.RoleOperate>
    {
        /// <summary>
        /// 获取角色下的所有操作
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        List<RoleOperateOutputDto> GetRoleOperateByRole(string roleid);

        /// <summary>
        /// 删除角色下的所有操作
        /// </summary>
        /// <param name="roleid"></param>
        bool DeleteRoleOperateByRole(string roleid);
    }
}