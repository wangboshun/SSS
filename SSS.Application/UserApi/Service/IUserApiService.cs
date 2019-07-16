using SSS.Domain.UserApi.Dto;
using SSS.Domain.Seedwork.Model;
using System.Collections.Generic;

namespace SSS.Application.UserApi.Service
{
    public interface IUserApiService
    {
        void AddUserApi(UserApiInputDto input);

		Pages<List<UserApiOutputDto>> GetListUserApi(UserApiInputDto input);

        UserApiOutputDto GetByUserId(UserApiInputDto input);
    }
}