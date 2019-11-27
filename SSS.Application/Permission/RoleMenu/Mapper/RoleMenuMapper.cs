using SSS.Domain.Permission.RoleMenu.Dto;

namespace SSS.Application.Permission.RoleMenu.Mapper
{
    public class RoleMenuProfile : AutoMapper.Profile
    {
        public RoleMenuProfile()
        {
            CreateMap<SSS.Domain.Permission.RoleMenu.RoleMenu, RoleMenuOutputDto>();

            CreateMap<RoleMenuInputDto, SSS.Domain.Permission.RoleMenu.RoleMenu>();
        }
    }
}
