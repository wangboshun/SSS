using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.Relation.PowerGroupRelation
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerGroupRelationRepository))]
    public class PowerGroupRelationRepository : Repository<Domain.Permission.Relation.PowerGroupRelation.PowerGroupRelation>, IPowerGroupRelationRepository
    {
        public PowerGroupRelationRepository(DbcontextBase context) : base(context)
        {
        }
    }
}