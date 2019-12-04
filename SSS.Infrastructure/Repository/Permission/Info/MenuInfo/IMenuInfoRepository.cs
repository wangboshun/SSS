using SSS.Domain.Permission.Info.MenuInfo.Dto;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Info.MenuInfo
{
    public interface IMenuInfoRepository : IRepository<Domain.Permission.Info.MenuInfo.MenuInfo>
    {
        /// <summary>
        /// 获取菜单下的所有下级
        /// </summary>
        /// <param name="menuid"></param>
        /// <returns></returns>
        List<MenuInfoTreeOutputDto> GetChildren(string menuid);
    }
}