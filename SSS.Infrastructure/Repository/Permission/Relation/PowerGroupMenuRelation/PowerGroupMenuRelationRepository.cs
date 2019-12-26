using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.Relation.PowerGroupMenuRelation
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerGroupMenuRelationRepository))]
    public class PowerGroupMenuRelationRepository : Repository<Domain.Permission.Relation.PowerGroupMenuRelation.PowerGroupMenuRelation>, IPowerGroupMenuRelationRepository
    {
        public PowerGroupMenuRelationRepository(SystemDbContext context) : base(context)
        {
        }
    }
}