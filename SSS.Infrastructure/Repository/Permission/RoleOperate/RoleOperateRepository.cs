using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.RoleOperate
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleOperateRepository))]
    public class RoleOperateRepository : Repository<SSS.Domain.Permission.RoleOperate.RoleOperate>, IRoleOperateRepository
    {
        public RoleOperateRepository(DbcontextBase context) : base(context)
        {
        }
    }
}