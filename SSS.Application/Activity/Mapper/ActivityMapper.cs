using SSS.Domain.Activity.Dto;

namespace SSS.Application.Activity.Mapper
{
    public class ActivityProfile : AutoMapper.Profile
    {
        public ActivityProfile()
        {
            CreateMap<SSS.Domain.Activity.Activity, ActivityOutputDto>();

            CreateMap<ActivityInputDto, SSS.Domain.Activity.Activity>();
        }
    }
}
