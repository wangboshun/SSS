using AutoMapper;

using SSS.Domain.Permission.UserInfo.Dto;

namespace SSS.Application.Permission.Info.UserInfo.Mapper
{
    public class UserInfoProfile : Profile
    {
        public UserInfoProfile()
        {
            CreateMap<Domain.Permission.Info.UserInfo.UserInfo, UserInfoOutputDto>();

            CreateMap<UserInfoInputDto, Domain.Permission.Info.UserInfo.UserInfo>();
        }
    }
}