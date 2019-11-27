using AutoMapper;

using SSS.Domain.Permission.UserInfo.Dto;

namespace SSS.Application.Permission.UserInfo.Mapper
{
    public class UserInfoProfile : Profile
    {
        public UserInfoProfile()
        {
            CreateMap<Domain.Permission.UserInfo.UserInfo, UserInfoOutputDto>();

            CreateMap<UserInfoInputDto, Domain.Permission.UserInfo.UserInfo>();
        }
    }
}