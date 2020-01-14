using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.Relation.UserGroupRoleGroupRelation
{
    [DIService(ServiceLifetime.Singleton, typeof(IUserGroupRoleGroupRelationRepository))]
    public class UserGroupRoleGroupRelationRepository : Repository<SSS.Domain.Permission.Relation.UserGroupRoleGroupRelation.UserGroupRoleGroupRelation>, IUserGroupRoleGroupRelationRepository
    {
        public UserGroupRoleGroupRelationRepository(PermissionDbContext context) : base(context)
        {
        }
    }
}