using SSS.Domain.UserActivity.Dto;
using SSS.Domain.Seedwork.Model;
using System.Collections.Generic;

namespace SSS.Application.UserActivity.Service
{
    public interface IUserActivityService
    {
        void AddUserActivity(UserActivityInputDto input);

		Pages<List<UserActivityOutputDto>> GetListUserActivity(UserActivityInputDto input);

        List<int> GetGroupNumberByName(UserActivityInputDto input);

    }
}