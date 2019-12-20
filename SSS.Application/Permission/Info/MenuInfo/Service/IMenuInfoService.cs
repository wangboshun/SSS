using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Info.MenuInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Info.MenuInfo.Service
{
    public interface IMenuInfoService : IQueryService<Domain.Permission.Info.MenuInfo.MenuInfo, MenuInfoInputDto,
        MenuInfoOutputDto>
    {
        MenuInfoOutputDto AddMenuInfo(MenuInfoInputDto input);
        bool UpdateMenuInfo(MenuInfoInputDto input);
        bool DeleteMenuInfo(string id);
        Pages<List<MenuInfoOutputDto>> GetListMenuInfo(MenuInfoInputDto input);
        /// <summary>
        ///     获取菜单下的所有下级
        /// </summary>
        /// <param name="menuid"></param>
        /// <returns></returns>
        List<MenuInfoTreeOutputDto> GetChildren(string menuid);

        /// <summary>
        /// 根据权限组Id或名称，遍历关联菜单
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<MenuInfoOutputDto>> GetMenuByPowerGroup(PowerGroupInputDto input);
    }
}