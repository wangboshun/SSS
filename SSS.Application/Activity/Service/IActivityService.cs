using SSS.Domain.Activity.Dto;
using SSS.Domain.Seedwork.Model;
using System.Collections.Generic;

namespace SSS.Application.Activity.Service
{
    public interface IActivityService
    {
        void AddActivity(ActivityInputDto input);

		Pages<List<ActivityOutputDto>> GetListActivity(ActivityInputDto input);
    }
}