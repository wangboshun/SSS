using SSS.Domain.UserActivity.Dto;

namespace SSS.Application.UserActivity.Mapper
{
    public class UserActivityProfile : AutoMapper.Profile
    {
        public UserActivityProfile()
        {
            CreateMap<SSS.Domain.UserActivity.UserActivity, UserActivityOutputDto>();

            CreateMap<UserActivityInputDto, SSS.Domain.UserActivity.UserActivity>();
        }
    }
}
