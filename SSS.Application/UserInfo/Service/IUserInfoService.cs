using SSS.Application.Seedwork.Service;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.UserInfo.Dto;

using System.Collections.Generic;

namespace SSS.Application.UserInfo.Service
{
    public interface IUserInfoService : IQueryService<Domain.UserInfo.UserInfo, UserInfoInputDto, UserInfoOutputDto>
    {
        void AddUserInfo(UserInfoInputDto input);

        UserInfoOutputDto GetByUserName(UserInfoInputDto input);

        Pages<List<UserInfoOutputDto>> GetListUserInfo(UserInfoInputDto input);
    }
}