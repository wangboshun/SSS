using SSS.Domain.Permission.UserRole.Dto;

namespace SSS.Application.Permission.UserRole.Mapper
{
    public class UserRoleProfile : AutoMapper.Profile
    {
        public UserRoleProfile()
        {
            CreateMap<SSS.Domain.Permission.UserRole.UserRole, UserRoleOutputDto>();

            CreateMap<UserRoleInputDto, SSS.Domain.Permission.UserRole.UserRole>();
        }
    }
}
