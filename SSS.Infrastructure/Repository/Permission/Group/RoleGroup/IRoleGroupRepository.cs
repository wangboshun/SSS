using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Group.RoleGroup
{
    public interface IRoleGroupRepository : IRepository<SSS.Domain.Permission.Group.RoleGroup.RoleGroup>
    {
        /// <summary>
        ///  根据角色Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="roleid"></param>
        /// <param name="rolename"></param>
        /// <param name="parentid"></param>
        /// <param name="pageindex"></param>
        /// <param name="pagesize"></param>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>> GetRoleGroupByRole(string roleid, string rolename, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        ///  根据权限组Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="powergroupid"></param>
        /// <param name="powergroupname"></param>
        /// <param name="parentid"></param>
        /// <param name="pageindex"></param>
        /// <param name="pagesize"></param>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>> GetMenuByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);
         
    }
}