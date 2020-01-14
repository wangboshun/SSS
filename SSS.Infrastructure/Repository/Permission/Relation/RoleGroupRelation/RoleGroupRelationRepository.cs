using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.Relation.RoleGroupRelation
{
    [DIService(ServiceLifetime.Singleton, typeof(IRoleGroupRelationRepository))]
    public class RoleGroupRelationRepository : Repository<SSS.Domain.Permission.Relation.RoleGroupRelation.RoleGroupRelation>, IRoleGroupRelationRepository
    {
        public RoleGroupRelationRepository(PermissionDbContext context) : base(context)
        {

        }
    }
}