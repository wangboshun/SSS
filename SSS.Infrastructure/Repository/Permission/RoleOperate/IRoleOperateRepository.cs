using SSS.Domain.Permission.RoleOperate.Dto;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.RoleOperate
{
    public interface IRoleOperateRepository : IRepository<SSS.Domain.Permission.RoleOperate.RoleOperate>
    {
        /// <summary>
        /// ��ȡ��ɫ�µ����в���
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        List<RoleOperateOutputDto> GetRoleOperateByRole(string roleid);

        /// <summary>
        /// ɾ����ɫ�µ����в���
        /// </summary>
        /// <param name="roleid"></param>
        bool DeleteRoleOperateByRole(string roleid);
    }
}