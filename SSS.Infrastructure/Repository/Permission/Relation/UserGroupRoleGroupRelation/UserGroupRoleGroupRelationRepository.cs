using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;

namespace SSS.Infrastructure.Repository.Permission.Relation.UserGroupRoleGroupRelation
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserGroupRoleGroupRelationRepository))]
    public class UserGroupRoleGroupRelationRepository : Repository<SSS.Domain.Permission.Relation.UserGroupRoleGroupRelation.UserGroupRoleGroupRelation>, IUserGroupRoleGroupRelationRepository
    {
        public UserGroupRoleGroupRelationRepository(DbcontextBase context) : base(context)
        {
        }
    }
}