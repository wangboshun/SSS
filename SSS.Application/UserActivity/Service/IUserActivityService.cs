using SSS.Application.Seedwork.Service;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.UserActivity.Dto;
using System.Collections.Generic;

namespace SSS.Application.UserActivity.Service
{
    public interface IUserActivityService : IQueryService<SSS.Domain.UserActivity.UserActivity, UserActivityInputDto, UserActivityOutputDto>
    {
        void AddUserActivity(UserActivityInputDto input);

        Pages<List<UserActivityOutputDto>> GetListUserActivity(UserActivityInputDto input);

        List<int> GetGroupNumber(UserActivityInputDto input);

    }
}