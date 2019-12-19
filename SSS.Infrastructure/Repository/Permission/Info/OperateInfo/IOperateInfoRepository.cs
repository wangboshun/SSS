using SSS.Domain.Permission.Info.OperateInfo;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Info.OperateInfo
{
    public interface IOperateInfoRepository : IRepository<Domain.Permission.Info.OperateInfo.OperateInfo>
    {
        /// <summary>
        /// 获取操作下的所有下级
        /// </summary>
        /// <param name="operateid"></param>
        /// <returns></returns>
        List<OperateInfoTreeOutputDto> GetChildrenById(string operateid);

        /// <summary>
        ///根据权限组Id或名称，遍历关联操作
        /// </summary> 
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.OperateInfo.OperateInfo>> GetOperateByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// 根据用户Id或名称，遍历关联操作
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.OperateInfo.OperateInfo>> GetOperateByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// 根据用户组Id或名称，遍历关联操作
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.OperateInfo.OperateInfo>> GetOperateByUserGroup(string usergroupid, string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// 根据角色组Id或名称，遍历关联操作
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.OperateInfo.OperateInfo>> GetOperateByRoleGroup(string rolegroupid, string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0);
    }
}