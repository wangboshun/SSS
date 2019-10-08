using SSS.Application.Seedwork.Service;
using SSS.Domain.Activity.Dto;
using SSS.Domain.Seedwork.Model;
using System.Collections.Generic;

namespace SSS.Application.Activity.Service
{
    public interface IActivityService : IQueryService<SSS.Domain.Activity.Activity, ActivityInputDto, ActivityOutputDto>
    {
        void AddActivity(ActivityInputDto input);

        Pages<List<ActivityOutputDto>> GetListActivity(ActivityInputDto input);

        ActivityOutputDto GetById(ActivityInputDto input);
    }
}