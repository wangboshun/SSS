using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.UserInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserInfoRepository))]
    public class UserInfoRepository : Repository<Domain.UserInfo.UserInfo>, IUserInfoRepository
    {
        public UserInfoRepository(DbcontextBase context) : base(context)
        {
        }
    }
}