using SSS.Domain.CQRS.UserActivity.Command.Commands;
using SSS.Domain.UserActivity.Dto;

namespace SSS.Application.UserActivity.Mapper
{
    public class UserActivityProfile : AutoMapper.Profile
    {
        public UserActivityProfile()
        {
            CreateMap<SSS.Domain.UserActivity.UserActivity, UserActivityOutputDto>();

            CreateMap<UserActivityInputDto, SSS.Domain.UserActivity.UserActivity>();

            CreateMap<UserActivityInputDto, UserActivityAddCommand>()
                .ConstructUsing(input => new UserActivityAddCommand(input));
        }
    }
}
