using SSS.Domain.CQRS.UserApi.Command.Commands;
using SSS.Domain.UserApi.Dto;

namespace SSS.Application.UserApi.Profile
{
    public class UserApiProfile : AutoMapper.Profile
    {
        public UserApiProfile()
        {
            CreateMap<SSS.Domain.UserApi.UserApi, UserApiOutputDto>();

            CreateMap<UserApiInputDto, UserApiAddCommand>()
                .ConstructUsing(input => new UserApiAddCommand(input));
        }
    }
}
