using SSS.Domain.Seedwork.Repository;

namespace SSS.Infrastructure.Repository.UserInfo
{
    public interface IUserInfoRepository : IRepository<SSS.Domain.UserInfo.UserInfo>
    {
        Domain.UserInfo.UserInfo GetUserInfoByOpenid(string openid);
    }
}