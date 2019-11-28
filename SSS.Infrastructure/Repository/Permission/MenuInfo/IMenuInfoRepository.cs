using SSS.Domain.Permission.MenuInfo.Dto;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.MenuInfo
{
    public interface IMenuInfoRepository : IRepository<SSS.Domain.Permission.MenuInfo.MenuInfo>
    {
        List<MenuInfoTreeOutputDto> GetChildren(MenuInfoInputDto input);
    }
}