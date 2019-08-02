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
        public Domain.UserInfo.UserInfo GetUserInfoByOpenid(string openid)
        {
            return DbSet.AsNoTracking().FirstOrDefault(x => x.Openid.Equals(openid));
        }
    }
}