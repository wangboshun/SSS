using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.Group.RoleGroup
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleGroupRepository))]
    public class RoleGroupRepository : Repository<SSS.Domain.Permission.Group.RoleGroup.RoleGroup>, IRoleGroupRepository
    {
        public RoleGroupRepository(DbcontextBase context) : base(context)
        {

        }

        /// <summary>
        /// 根据角色Id或名称，遍历关联角色组
        /// </summary> 
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>> GetRoleGroupByRole(string roleid, string rolename, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = " rg.* ";

            string sql = @"SELECT {0}  FROM
	                RoleInfo AS r
	                INNER JOIN RoleGroupRelation AS rrr ON r.id = rrr.RoleId
	                INNER JOIN RoleGroup AS rg ON rrr.RoleGroupId = rg.Id 
                WHERE
	   	            r.IsDelete=0 
	                AND rg.IsDelete=0 
	                AND rrr.IsDelete=0 ";

            if (!string.IsNullOrWhiteSpace(roleid))
                sql += $" AND r.Id='{roleid}'";

            if (!string.IsNullOrWhiteSpace(rolename))
                sql += $" AND r.RoleName='{rolename}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND r.ParentId='{parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            if (pageindex > 0 && pagesize > 0)
            {
                string limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<Domain.Permission.Group.RoleGroup.RoleGroup>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>>(data, count);
            }
            else
            {
                var data = Db.Database.SqlQuery<Domain.Permission.Group.RoleGroup.RoleGroup>(string.Format(sql, field));
                return new Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>>(data, count);
            }
        }

        /// <summary>
        ///  根据权限组Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="powergroupid"></param>
        /// <param name="powergroupname"></param>
        /// <param name="parentid"></param>
        /// <param name="pageindex"></param>
        /// <param name="pagesize"></param>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>> GetRoleGroupByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = " rg.* ";

            string sql = @"SELECT {0}  FROM
	                RoleGroup AS rg 
	                INNER JOIN RoleGroupPowerGroupRelation AS rgpgr ON rgpgr.RoleGroupId = rg.Id
	                INNER JOIN PowerGroup AS pg ON pg.Id = rgpgr.PowerGroupId
                WHERE
	                rg.IsDelete = 0  
	                AND rgpgr.IsDelete = 0 
	                AND pg.IsDelete = 0";

            if (!string.IsNullOrWhiteSpace(powergroupid))
                sql += $" AND pg.Id='{powergroupid}'";

            if (!string.IsNullOrWhiteSpace(powergroupname))
                sql += $" AND pg.PowerGroupName='{powergroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND pg.ParentId='{parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            if (pageindex > 0 && pagesize > 0)
            {
                string limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<Domain.Permission.Group.RoleGroup.RoleGroup>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>>(data, count);
            }
            else
            {
                var data = Db.Database.SqlQuery<Domain.Permission.Group.RoleGroup.RoleGroup>(string.Format(sql, field));
                return new Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>>(data, count);
            }
        }

        /// <summary>
        /// 根据用户组Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="usergroupid"></param>
        /// <param name="usergroupname"></param>
        /// <param name="parentid"></param>
        /// <param name="pageindex"></param>
        /// <param name="pagesize"></param>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>> GetRoleGroupByUserGroup(string usergroupid, string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = " rg.* ";

            string sql = @"SELECT {0}  FROM
	                UserGroup ug
	                INNER JOIN UserGroupRoleGroupRelation AS ugrgr ON ug.Id = ugrgr.UserGroupId
	                INNER JOIN RoleGroup AS rg ON rg.Id = ugrgr.RoleGroupId 
                WHERE
	                ug.IsDelete = 0 
	                AND ugrgr.IsDelete = 0 
	                AND rg.IsDelete =0 ";

            if (!string.IsNullOrWhiteSpace(usergroupid))
                sql += $" AND ug.Id='{usergroupid}'";

            if (!string.IsNullOrWhiteSpace(usergroupname))
                sql += $" AND ug.UserGroupName='{usergroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND ug.ParentId='{parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            if (pageindex > 0 && pagesize > 0)
            {
                string limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<Domain.Permission.Group.RoleGroup.RoleGroup>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>>(data, count);
            }
            else
            {
                var data = Db.Database.SqlQuery<Domain.Permission.Group.RoleGroup.RoleGroup>(string.Format(sql, field));
                return new Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>>(data, count);
            }
        }

        /// <summary>
        /// 根据用户Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="userid"></param>
        /// <param name="username"></param>
        /// <param name="parentid"></param>
        /// <param name="pageindex"></param>
        /// <param name="pagesize"></param>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>> GetRoleGroupByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = " rg.* ";

            string sql = @"SELECT {0}  FROM
	                UserInfo AS u
	                INNER JOIN UserGroupRelation ugr ON u.Id = ugr.UserId 
	                INNER JOIN UserGroupRoleGroupRelation AS ugrgr ON ugr.UserGroupId = ugrgr.UserGroupId
	                INNER JOIN RoleGroup AS rg ON rg.Id = ugrgr.RoleGroupId 
                WHERE  ugrgr.IsDelete = 0 
	                AND rg.IsDelete =0 ";

            if (!string.IsNullOrWhiteSpace(userid))
                sql += $" AND u.Id='{userid}'";

            if (!string.IsNullOrWhiteSpace(username))
                sql += $" AND u.UserName='{username}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND u.ParentId='{parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            if (pageindex > 0 && pagesize > 0)
            {
                string limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<Domain.Permission.Group.RoleGroup.RoleGroup>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>>(data, count);
            }
            else
            {
                var data = Db.Database.SqlQuery<Domain.Permission.Group.RoleGroup.RoleGroup>(string.Format(sql, field));
                return new Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>>(data, count);
            }
        }
    }
}