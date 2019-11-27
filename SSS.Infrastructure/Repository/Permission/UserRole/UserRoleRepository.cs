using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.UserRole
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserRoleRepository))]
    public class UserRoleRepository : Repository<SSS.Domain.Permission.UserRole.UserRole>, IUserRoleRepository
    {
        public UserRoleRepository(DbcontextBase context) : base(context)
        {
        }
    }
}