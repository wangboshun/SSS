using AutoMapper;

using SSS.Domain.UserActivity.Dto;

namespace SSS.Application.UserActivity.Mapper
{
    public class UserActivityProfile : Profile
    {
        public UserActivityProfile()
        {
            CreateMap<Domain.UserActivity.UserActivity, UserActivityOutputDto>();

            CreateMap<UserActivityInputDto, Domain.UserActivity.UserActivity>();
        }
    }
}