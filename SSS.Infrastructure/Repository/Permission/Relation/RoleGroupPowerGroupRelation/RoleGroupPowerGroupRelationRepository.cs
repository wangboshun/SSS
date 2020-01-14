using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.Relation.RoleGroupPowerGroupRelation
{
    [DIService(ServiceLifetime.Singleton, typeof(IRoleGroupPowerGroupRelationRepository))]
    public class RoleGroupPowerGroupRelationRepository : Repository<SSS.Domain.Permission.Relation.RoleGroupPowerGroupRelation.RoleGroupPowerGroupRelation>, IRoleGroupPowerGroupRelationRepository
    {
        public RoleGroupPowerGroupRelationRepository(PermissionDbContext context) : base(context)
        {
        }
    }
}