using SSS.Domain.Permission.RoleOperate.Dto;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.RoleOperate
{
    public interface IRoleOperateRepository : IRepository<SSS.Domain.Permission.RoleOperate.RoleOperate>
    {
        List<RoleOperateOutputDto> GetOperateByRole(string roleid);
    }
}