using SSS.Domain.Permission.UserInfo.Dto;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.UserInfo
{
    public interface IUserInfoRepository : IRepository<Domain.Permission.UserInfo.UserInfo>
    {
        List<UserInfoTreeOutputDto> GetChildren(UserInfoInputDto input);
    }
}