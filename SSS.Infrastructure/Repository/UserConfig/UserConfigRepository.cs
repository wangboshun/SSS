using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;

namespace SSS.Infrastructure.Repository.UserConfig
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserConfigRepository))]
    public class UserConfigRepository : Repository<SSS.Domain.UserConfig.UserConfig>, IUserConfigRepository
    {
        public UserConfigRepository(DbcontextBase context) : base(context)
        {
        }
    }
}