using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Group.UserGroup
{
    public interface IUserGroupRepository : IRepository<SSS.Domain.Permission.Group.UserGroup.UserGroup>
    {
        /// <summary>
        ///  根据用户Id或名称，遍历关联用户组
        /// </summary>
        /// <param name="userid"></param>
        /// <param name="username"></param>
        /// <param name="parentid"></param>
        /// <param name="pageindex"></param>
        /// <param name="pagesize"></param>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Group.UserGroup.UserGroup>> GetUserGroupByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        ///  根据权限组Id或名称，遍历关联用户组
        /// </summary>
        /// <param name="powergroupid"></param>
        /// <param name="powergroupname"></param>
        /// <param name="parentid"></param>
        /// <param name="pageindex"></param>
        /// <param name="pagesize"></param>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Group.UserGroup.UserGroup>> GetUserGroupByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        ///  根据角色组Id或名称，遍历关联用户组
        /// </summary>
        /// <param name="rolegroupid"></param>
        /// <param name="rolegroupname"></param>
        /// <param name="parentid"></param>
        /// <param name="pageindex"></param>
        /// <param name="pagesize"></param>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Group.UserGroup.UserGroup>> GetUserGroupByRoleGroup(string rolegroupid, string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

    }
}