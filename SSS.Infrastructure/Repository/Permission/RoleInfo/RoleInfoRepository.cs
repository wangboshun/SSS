using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.RoleInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleInfoRepository))]
    public class RoleInfoRepository : Repository<SSS.Domain.Permission.RoleInfo.RoleInfo>, IRoleInfoRepository
    {
        public RoleInfoRepository(DbcontextBase context) : base(context)
        {
        }
    }
}