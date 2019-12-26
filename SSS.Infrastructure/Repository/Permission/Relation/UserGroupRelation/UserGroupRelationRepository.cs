using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.Relation.UserGroupRelation
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserGroupRelationRepository))]
    public class UserGroupRelationRepository : Repository<SSS.Domain.Permission.Relation.UserGroupRelation.UserGroupRelation>, IUserGroupRelationRepository
    {
        public UserGroupRelationRepository(PermissionDbContext context) : base(context)
        {
        }
    }
}