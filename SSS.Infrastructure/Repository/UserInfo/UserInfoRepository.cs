using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;
using System.Linq;

namespace SSS.Infrastructure.Repository.UserInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserInfoRepository))]
    public class UserInfoRepository : Repository<SSS.Domain.UserInfo.UserInfo>, IUserInfoRepository
    {
        public UserInfoRepository(DbcontextBase context) : base(context)
        {
        }

        public Domain.UserInfo.UserInfo GetUserInfoByPhone(string phone)
        {
            return Get(x => x.Phone.Equals(phone));
        }

        public Domain.UserInfo.UserInfo GetUserInfoByUid(string uid)
        {
            return DbSet.AsNoTracking().FirstOrDefault(x => x.Uid.Equals(uid));
        }
    }
}