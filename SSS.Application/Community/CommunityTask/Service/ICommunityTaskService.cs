using SSS.Application.Seedwork.Service;
using SSS.Domain.Community.CommunityTask.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Community.CommunityTask.Service
{
    public interface ICommunityTaskService : IQueryService<SSS.Domain.Community.CommunityTask.CommunityTask, CommunityTaskInputDto, CommunityTaskOutputDto>
    {
        CommunityTaskOutputDto AddCommunityTask(CommunityTaskInputDto input);

        Pages<List<CommunityTaskOutputDto>> GetListCommunityTask(CommunityTaskInputDto input);
    }
}