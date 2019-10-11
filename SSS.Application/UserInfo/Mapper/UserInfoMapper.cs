using SSS.Domain.UserInfo.Dto;

namespace SSS.Application.UserInfo.Mapper
{
    public class UserInfoProfile : AutoMapper.Profile
    {
        public UserInfoProfile()
        {
            CreateMap<SSS.Domain.UserInfo.UserInfo, UserInfoOutputDto>();

            CreateMap<UserInfoInputDto, SSS.Domain.UserInfo.UserInfo>();
        }
    }
}
