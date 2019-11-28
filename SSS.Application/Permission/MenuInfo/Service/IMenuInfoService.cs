using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.MenuInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.MenuInfo.Service
{
    public interface IMenuInfoService : IQueryService<SSS.Domain.Permission.MenuInfo.MenuInfo, MenuInfoInputDto, MenuInfoOutputDto>
    {
        void AddMenuInfo(MenuInfoInputDto input);
        Pages<List<MenuInfoOutputDto>> GetListMenuInfo(MenuInfoInputDto input);
        List<MenuInfoTreeOutputDto> GetChildren(MenuInfoInputDto input);
    }
}