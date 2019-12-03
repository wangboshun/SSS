using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.Group.PowerGroup
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerGroupRepository))]
    public class PowerGroupRepository : Repository<SSS.Domain.Permission.Group.PowerGroup.PowerGroup>, IPowerGroupRepository
    {
        public PowerGroupRepository(DbcontextBase context) : base(context)
        {
        }
    }
}