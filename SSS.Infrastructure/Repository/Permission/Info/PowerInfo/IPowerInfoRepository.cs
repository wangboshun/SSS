using SSS.Domain.Permission.Info.PowerInfo.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Info.PowerInfo
{
    public interface IPowerInfoRepository : IRepository<Domain.Permission.Info.PowerInfo.PowerInfo>
    {
        /// <summary>
        /// 获取权限下的所有下级
        /// </summary>
        /// <param name="powerid"></param>
        /// <returns></returns>
        List<PowerInfoTreeOutputDto> GetChildren(string powerid);

        /// <summary>
        /// 根据用户Id或名称，遍历关联权限
        /// </summary>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.PowerInfo.PowerInfo>> GetPowerByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// 根据用户组Id或名称，遍历关联权限
        /// </summary>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.PowerInfo.PowerInfo>> GetPowerByUserGroup(string usergroupid, string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// 根据权限组Id或名称，遍历关联权限
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.PowerInfo.PowerInfo>> GetPowerByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// 根据角色组Id或名称，遍历关联权限
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.PowerInfo.PowerInfo>> GetPowerByRoleGroup(string rolegroupid, string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

    }
}