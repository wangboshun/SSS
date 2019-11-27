using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.RoleMenu
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleMenuRepository))]
    public class RoleMenuRepository : Repository<SSS.Domain.Permission.RoleMenu.RoleMenu>, IRoleMenuRepository
    {
        public RoleMenuRepository(DbcontextBase context) : base(context)
        {
        }
    }
}