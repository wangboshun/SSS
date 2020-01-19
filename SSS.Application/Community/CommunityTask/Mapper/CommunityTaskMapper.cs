using SSS.Domain.Community.CommunityTask.Dto;

namespace SSS.Application.Community.CommunityTask.Mapper
{
    public class CommunityTaskProfile : AutoMapper.Profile
    {
        public CommunityTaskProfile()
        {
            CreateMap<SSS.Domain.Community.CommunityTask.CommunityTask, CommunityTaskOutputDto>();

            CreateMap<CommunityTaskInputDto, SSS.Domain.Community.CommunityTask.CommunityTask>();
        }
    }
}