using SSS.Domain.UserInfo.Dto;
using SSS.Domain.Seedwork.Model;
using System.Collections.Generic;
using SSS.Application.Seedwork.Service;

namespace SSS.Application.UserInfo.Service
{
    public interface IUserInfoService:IQueryService<SSS.Domain.UserInfo.UserInfo, UserInfoInputDto, UserInfoOutputDto>
    {
        void AddUserInfo(UserInfoInputDto input);

        UserInfoOutputDto GetByUserName(UserInfoInputDto input);

        Pages<List<UserInfoOutputDto>> GetListUserInfo(UserInfoInputDto input);
    }
}