using AutoMapper;

using SSS.Domain.Permission.Group.UserGroup.Dto;

namespace SSS.Application.Permission.Group.UserGroup.Mapper
{
    public class UserGroupProfile : Profile
    {
        public UserGroupProfile()
        {
            CreateMap<Domain.Permission.Group.UserGroup.UserGroup, UserGroupOutputDto>();

            CreateMap<UserGroupInputDto, Domain.Permission.Group.UserGroup.UserGroup>();
        }
    }
}