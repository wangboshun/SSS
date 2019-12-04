using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Info.MenuInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Info.MenuInfo.Service
{
    public interface IMenuInfoService : IQueryService<Domain.Permission.Info.MenuInfo.MenuInfo, MenuInfoInputDto,
        MenuInfoOutputDto>
    {
        void AddMenuInfo(MenuInfoInputDto input);
        void DeleteMenuInfo(MenuInfoInputDto input);
        Pages<List<MenuInfoOutputDto>> GetListMenuInfo(MenuInfoInputDto input); 
        /// <summary>
        ///     获取菜单下的所有下级
        /// </summary>
        /// <param name="menuid"></param>
        /// <returns></returns>
        List<MenuInfoTreeOutputDto> GetChildren(string menuid);
    }
}