using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.Relation.PowerMenuRelation
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerMenuRelationRepository))]
    public class PowerMenuRelationRepository : Repository<Domain.Permission.Relation.PowerMenuRelation.PowerMenuRelation>, IPowerMenuRelationRepository
    {
        public PowerMenuRelationRepository(DbcontextBase context) : base(context)
        {
        }
    }
}