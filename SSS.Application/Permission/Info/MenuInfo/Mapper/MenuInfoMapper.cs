using AutoMapper;

using SSS.Domain.Permission.Info.MenuInfo.Dto;

namespace SSS.Application.Permission.Info.MenuInfo.Mapper
{
    public class MenuInfoProfile : Profile
    {
        public MenuInfoProfile()
        {
            CreateMap<Domain.Permission.Info.MenuInfo.MenuInfo, MenuInfoOutputDto>();

            CreateMap<MenuInfoInputDto, Domain.Permission.Info.MenuInfo.MenuInfo>();
        }
    }
}