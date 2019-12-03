using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.Group.RoleGroup
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleGroupRepository))]
    public class RoleGroupRepository : Repository<SSS.Domain.Permission.Group.RoleGroup.RoleGroup>, IRoleGroupRepository
    {
        public RoleGroupRepository(DbcontextBase context) : base(context)
        {
        }
    }
}