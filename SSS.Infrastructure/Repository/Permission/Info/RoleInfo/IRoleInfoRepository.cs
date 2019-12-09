using SSS.Domain.Permission.Info.RoleInfo.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Info.RoleInfo
{
    public interface IRoleInfoRepository : IRepository<Domain.Permission.Info.RoleInfo.RoleInfo>
    {
        /// <summary>
        /// 获取角色下的所有下级
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        List<RoleInfoTreeOutputDto> GetChildren(string roleid);

        /// <summary>
        /// 根据角色组Id或名称，遍历关联角色
        /// </summary>
        /// <param name="rolegroupid"></param>
        /// <param name="rolegroupname"></param>
        /// <param name="parentid"></param>
        /// <param name="pageindex"></param>
        /// <param name="pagesize"></param>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.RoleInfo.RoleInfo>> GetRoleByRoleGroup(string rolegroupid, string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// 根据权限组Id或名称，遍历关联角色
        /// </summary>
        /// <param name="powergroupid"></param>
        /// <param name="powergroupname"></param>
        /// <param name="parentid"></param>
        /// <param name="pageindex"></param>
        /// <param name="pagesize"></param>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.RoleInfo.RoleInfo>> GetRoleByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

    }
}