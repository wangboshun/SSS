using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.Relation.RoleUserGroupRelation
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleUserGroupRelationRepository))]
    public class RoleUserGroupRelationRepository : Repository<SSS.Domain.Permission.Relation.RoleUserGroupRelation.RoleUserGroupRelation>, IRoleUserGroupRelationRepository
    {
        public RoleUserGroupRelationRepository(DbcontextBase context) : base(context)
        {
        }
    }
}