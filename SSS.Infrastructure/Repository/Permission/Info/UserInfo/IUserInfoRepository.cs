using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;
using SSS.Domain.Permission.Info.UserInfo.Dto;

namespace SSS.Infrastructure.Repository.Permission.Info.UserInfo
{
    public interface IUserInfoRepository : IRepository<Domain.Permission.Info.UserInfo.UserInfo>
    {
        /// <summary>
        /// 获取用户下的所有下级
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        List<UserInfoTreeOutputDto> GetChildrenById(string userid);
    }
}