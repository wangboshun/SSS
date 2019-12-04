using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.Relation.PowerOperateRelation
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerOperateRelationRepository))]
    public class PowerOperateRelationRepository : Repository<Domain.Permission.Relation.PowerOperateRelation.PowerOperateRelation>, IPowerOperateRelationRepository
    {
        public PowerOperateRelationRepository(DbcontextBase context) : base(context)
        {
        }
    }
}