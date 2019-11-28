using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.RoleOperate.Dto;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.RoleOperate
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleOperateRepository))]
    public class RoleOperateRepository : Repository<SSS.Domain.Permission.RoleOperate.RoleOperate>, IRoleOperateRepository
    {
        public RoleOperateRepository(DbcontextBase context) : base(context)
        {
        }
        public List<RoleOperateOutputDto> GetOperateByRole(string roleid)
        {
            var result = Db.RoleOperate.Where(x => x.RoleId.Equals(roleid) && x.IsDelete == 0).Join(Db.OperateInfo,
                role => role.OperateId, operate => operate.Id,
                (role, operate) => new RoleOperateOutputDto
                {
                    id = role.Id,
                    operateid = operate.Id,
                    operatename = operate.OperateName,
                    createtime = role.CreateTime
                }).ToList();
            return result;
        }
    }
}