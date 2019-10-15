using AutoMapper;
using SSS.Domain.UserInfo.Dto;

namespace SSS.Application.UserInfo.Mapper
{
    public class UserInfoProfile : Profile
    {
        public UserInfoProfile()
        {
            CreateMap<Domain.UserInfo.UserInfo, UserInfoOutputDto>();

            CreateMap<UserInfoInputDto, Domain.UserInfo.UserInfo>();
        }
    }
}