using SSS.Domain.Activity.Dto;
using SSS.Domain.CQRS.Activity.Command.Commands;

namespace SSS.Application.Activity.Mapper
{
    public class ActivityProfile : AutoMapper.Profile
    {
        public ActivityProfile()
        {
            CreateMap<SSS.Domain.Activity.Activity, ActivityOutputDto>();

            CreateMap<ActivityInputDto, ActivityAddCommand>()
                .ConstructUsing(input => new ActivityAddCommand(input));
        }
    }
}
