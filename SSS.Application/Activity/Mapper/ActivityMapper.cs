using AutoMapper;

using SSS.Domain.Activity.Dto;

namespace SSS.Application.Activity.Mapper
{
    public class ActivityProfile : Profile
    {
        public ActivityProfile()
        {
            CreateMap<Domain.Activity.Activity, ActivityOutputDto>();

            CreateMap<ActivityInputDto, Domain.Activity.Activity>();
        }
    }
}