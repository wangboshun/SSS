using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.Relation.PowerGroupOperateRelation
{
    [DIService(ServiceLifetime.Singleton, typeof(IPowerGroupOperateRelationRepository))]
    public class PowerGroupOperateRelationRepository : Repository<Domain.Permission.Relation.PowerGroupOperateRelation.PowerGroupOperateRelation>, IPowerGroupOperateRelationRepository
    {
        public PowerGroupOperateRelationRepository(PermissionDbContext context) : base(context)
        {
        }
    }
}