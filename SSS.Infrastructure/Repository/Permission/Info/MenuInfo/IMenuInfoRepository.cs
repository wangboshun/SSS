using SSS.Domain.Permission.Info.MenuInfo.Dto;
using SSS.Domain.Seedwork.Model;
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

        /// <summary>
        ///根据权限组Id或名称，遍历关联菜单
        /// </summary>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.MenuInfo.MenuInfo>> GetMenuByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// 根据角色组Id或名称，遍历关联菜单
        /// </summary>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.MenuInfo.MenuInfo>> GetMenuByRoleGroup(string rolegroupid, string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        ///根据用户Id或名称，遍历关联菜单
        /// </summary>
        /// <param name="userid"></param>
        /// <param name="username"></param>
        /// <param name="parentid"></param>
        /// <param name="pageindex"></param>
        /// <param name="pagesize"></param>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.MenuInfo.MenuInfo>> GetMenuByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        ///根据用户组Id或名称，遍历关联菜单
        /// </summary>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.MenuInfo.MenuInfo>> GetMenuByUserGroup(string usergroupid, string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);
    }
}