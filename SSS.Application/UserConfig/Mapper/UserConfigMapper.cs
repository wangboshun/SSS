using SSS.Domain.CQRS.UserConfig.Command.Commands;
using SSS.Domain.UserConfig.Dto;

namespace SSS.Application.UserConfig.Profile
{
    public class UserConfigProfile : AutoMapper.Profile
    {
        public UserConfigProfile()
        {
            CreateMap<SSS.Domain.UserConfig.UserConfig, UserConfigOutputDto>();

            CreateMap<UserConfigInputDto, UserConfigAddCommand>()
                .ConstructUsing(input => new UserConfigAddCommand(input));
        }
    }
}
