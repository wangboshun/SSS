using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;

namespace SSS.Infrastructure.Repository.Permission.Relation.RoleGroupPowerGroupRelation
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleGroupPowerGroupRelationRepository))]
    public class RoleGroupPowerGroupRelationRepository : Repository<SSS.Domain.Permission.Relation.RoleGroupPowerGroupRelation.RoleGroupPowerGroupRelation>, IRoleGroupPowerGroupRelationRepository
    {
        public RoleGroupPowerGroupRelationRepository(DbcontextBase context) : base(context)
        {
        }
    }
}