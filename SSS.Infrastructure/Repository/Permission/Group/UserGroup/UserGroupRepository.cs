using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.Group.UserGroup
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserGroupRepository))]
    public class UserGroupRepository : Repository<SSS.Domain.Permission.Group.UserGroup.UserGroup>, IUserGroupRepository
    {
        public UserGroupRepository(DbcontextBase context) : base(context)
        {
        }
    }
}