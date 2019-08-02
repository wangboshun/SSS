using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.UserApi
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserApiRepository))]
    public class UserApiRepository : Repository<SSS.Domain.UserApi.UserApi>, IUserApiRepository
    {
        public UserApiRepository(DbcontextBase context) : base(context)
        {
        }
    }
}