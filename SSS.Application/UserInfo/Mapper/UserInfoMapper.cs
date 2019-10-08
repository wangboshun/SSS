using SSS.Domain.CQRS.UserInfo.Command.Commands;
using SSS.Domain.UserInfo.Dto;

namespace SSS.Application.UserInfo.Mapper
{
    public class UserInfoProfile : AutoMapper.Profile
    {
        public UserInfoProfile()
        {
            CreateMap<SSS.Domain.UserInfo.UserInfo, UserInfoOutputDto>();

            CreateMap<UserInfoInputDto, SSS.Domain.UserInfo.UserInfo>();

            CreateMap<UserInfoInputDto, UserInfoAddCommand>()
                .ConstructUsing(input => new UserInfoAddCommand(input));
        }
    }
}
