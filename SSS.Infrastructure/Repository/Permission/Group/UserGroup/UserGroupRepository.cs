using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Group.UserGroup
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserGroupRepository))]
    public class UserGroupRepository : Repository<SSS.Domain.Permission.Group.UserGroup.UserGroup>, IUserGroupRepository
    {
        private static string field = "ug";

        public UserGroupRepository(DbcontextBase context) : base(context)
        {
        }

        /// <summary>
        /// 根据用户Id或名称，遍历关联用户组
        /// </summary> 
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Group.UserGroup.UserGroup>> GetUserGroupByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string sql = @"SELECT {0}  FROM
	               	UserInfo AS u
	                INNER JOIN UserGroupRelation AS ugr ON u.id = ugr.UserId
	                INNER JOIN UserGroup AS ug ON ugr.UserGroupId = ug.Id 
                WHERE
	                u.IsDelete = 0 
	                AND ug.IsDelete = 0 
	                AND ugr.IsDelete = 0  ";

            if (!string.IsNullOrWhiteSpace(userid))
                sql += $" AND u.Id='{userid}'";

            if (!string.IsNullOrWhiteSpace(username))
                sql += $" AND u.UserName='{username}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND u.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联用户组
        /// </summary> 
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Group.UserGroup.UserGroup>> GetUserGroupByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string sql = @"SELECT {0} FROM
	               	UserGroup AS ug
	                INNER JOIN UserGroupRoleGroupRelation AS ugrgr ON ugrgr.UserGroupId = ug.Id
	                INNER JOIN RoleGroupPowerGroupRelation AS rgpgr ON rgpgr.RoleGroupId = ugrgr.RoleGroupId
	                INNER JOIN PowerGroup AS pg ON pg.Id = rgpgr.PowerGroupId
                WHERE
	                ug.IsDelete = 0 
	                AND ugrgr.IsDelete = 0  
	                AND rgpgr.IsDelete = 0 
	                AND pg.IsDelete = 0";

            if (!string.IsNullOrWhiteSpace(powergroupid))
                sql += $" AND pg.Id='{powergroupid}'";

            if (!string.IsNullOrWhiteSpace(powergroupname))
                sql += $" AND pg.PowerGroupName='{powergroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND pg.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据角色组Id或名称，遍历关联用户组
        /// </summary> 
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Group.UserGroup.UserGroup>> GetUserGroupByRoleGroup(string rolegroupid, string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string sql = @"SELECT {0} FROM
	               	UserGroup AS ug
	                INNER JOIN UserGroupRoleGroupRelation AS ugrgr ON ugrgr.UserGroupId = ug.Id
	                INNER JOIN RoleGroup AS rg ON ugrgr.RoleGroupId = rg.Id 
                WHERE
	                ug.IsDelete = 0 
	                AND ugrgr.IsDelete = 0 
	                AND rg.IsDelete = 0";

            if (!string.IsNullOrWhiteSpace(rolegroupid))
                sql += $" AND rg.Id='{rolegroupid}'";

            if (!string.IsNullOrWhiteSpace(rolegroupname))
                sql += $" AND rg.RoleGroupName='{rolegroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND rg.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }
    }
}