using SSS.Domain.Permission.MenuInfo.Dto;

namespace SSS.Application.Permission.MenuInfo.Mapper
{
    public class MenuInfoProfile : AutoMapper.Profile
    {
        public MenuInfoProfile()
        {
            CreateMap<SSS.Domain.Permission.MenuInfo.MenuInfo, MenuInfoOutputDto>();

            CreateMap<MenuInfoInputDto, SSS.Domain.Permission.MenuInfo.MenuInfo>();
        }
    }
}
