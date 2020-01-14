using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.Relation.PowerGroupRelation
{
    [DIService(ServiceLifetime.Singleton, typeof(IPowerGroupRelationRepository))]
    public class PowerGroupRelationRepository : Repository<Domain.Permission.Relation.PowerGroupRelation.PowerGroupRelation>, IPowerGroupRelationRepository
    {
        public PowerGroupRelationRepository(PermissionDbContext context) : base(context)
        {
        }
    }
}