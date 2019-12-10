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
        public UserGroupRepository(DbcontextBase context) : base(context)
        {
        }

        /// <summary>
        /// 根据用户Id或名称，遍历关联用户组
        /// </summary> 
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Group.UserGroup.UserGroup>> GetUserGroupByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = " DISTINCT ur.* ";

            string sql = @"SELECT {0}  FROM
	                UserInfo AS u
	                INNER JOIN UserGroupRelation AS uur ON u.id=uur.UserId
	                INNER JOIN UserGroup AS ur ON uur.UserGroupId=ur.Id 
                WHERE
	                u.IsDelete=0 
	                AND ur.IsDelete=0 
	                AND uur.IsDelete=0 ";

            if (!string.IsNullOrWhiteSpace(userid))
                sql += $" AND u.Id='{userid}'";

            if (!string.IsNullOrWhiteSpace(username))
                sql += $" AND u.UserName='{username}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND u.ParentId='{parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count( DISTINCT ur.Id ) "));

            if (pageindex > 0 && pagesize > 0)
            {
                string limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<Domain.Permission.Group.UserGroup.UserGroup>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<IEnumerable<Domain.Permission.Group.UserGroup.UserGroup>>(data, count);
            }
            else
            {
                var data = Db.Database.SqlQuery<Domain.Permission.Group.UserGroup.UserGroup>(string.Format(sql, field));
                return new Pages<IEnumerable<Domain.Permission.Group.UserGroup.UserGroup>>(data, count);
            }
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联用户组
        /// </summary>
        /// <param name="powergroupid"></param>
        /// <param name="powergroupname"></param>
        /// <param name="parentid"></param>
        /// <param name="pageindex"></param>
        /// <param name="pagesize"></param>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Group.UserGroup.UserGroup>> GetUserGroupByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = @" DISTINCT ug.* ";

            string sql = @"SELECT {0} FROM
	               	UserGroup AS ug
	                INNER JOIN UserGroupRoleGroupRelation AS rgugr ON rgugr.UserGroupId = ug.Id
	                INNER JOIN RoleGroupPowerGroupRelation AS rgpgr ON rgpgr.RoleGroupId = rgugr.RoleGroupId
	                INNER JOIN PowerGroup AS pg ON pg.Id = rgpgr.PowerGroupId
                WHERE
	                ug.IsDelete = 0 
	                AND rgugr.IsDelete = 0  
	                AND rgpgr.IsDelete = 0 
	                AND pg.IsDelete = 0";

            if (!string.IsNullOrWhiteSpace(powergroupid))
                sql += $" AND pg.Id='{powergroupid}'";

            if (!string.IsNullOrWhiteSpace(powergroupname))
                sql += $" AND pg.PowerGroupName='{powergroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND pg.ParentId='{parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count( DISTINCT ur.Id ) "));

            if (pageindex > 0 && pagesize > 0)
            {
                string limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<Domain.Permission.Group.UserGroup.UserGroup>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<IEnumerable<Domain.Permission.Group.UserGroup.UserGroup>>(data, count);
            }
            else
            {
                var data = Db.Database.SqlQuery<Domain.Permission.Group.UserGroup.UserGroup>(string.Format(sql, field));
                return new Pages<IEnumerable<Domain.Permission.Group.UserGroup.UserGroup>>(data, count);
            }
        }

        /// <summary>
        /// 根据角色组Id或名称，遍历关联用户组
        /// </summary>
        /// <param name="rolegroupid"></param>
        /// <param name="rolegroupname"></param>
        /// <param name="parentid"></param>
        /// <param name="pageindex"></param>
        /// <param name="pagesize"></param>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Group.UserGroup.UserGroup>> GetUserGroupByRoleGroup(string rolegroupid, string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = @" DISTINCT ug.* ";

            string sql = @"SELECT {0} FROM
	               	UserGroup AS ug
	                INNER JOIN UserGroupRoleGroupRelation AS rgugr ON rgugr.UserGroupId = ug.Id
	                INNER JOIN RoleGroup AS rg ON rgugr.RoleGroupId = rg.Id 
                WHERE
	                ug.IsDelete = 0 
	                AND rgugr.IsDelete = 0 
	                AND rg.IsDelete = 0";

            if (!string.IsNullOrWhiteSpace(rolegroupid))
                sql += $" AND rg.Id='{rolegroupid}'";

            if (!string.IsNullOrWhiteSpace(rolegroupname))
                sql += $" AND rg.RoleGroupName='{rolegroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND rg.ParentId='{parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count( DISTINCT ur.Id ) "));

            if (pageindex > 0 && pagesize > 0)
            {
                string limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<Domain.Permission.Group.UserGroup.UserGroup>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<IEnumerable<Domain.Permission.Group.UserGroup.UserGroup>>(data, count);
            }
            else
            {
                var data = Db.Database.SqlQuery<Domain.Permission.Group.UserGroup.UserGroup>(string.Format(sql, field));
                return new Pages<IEnumerable<Domain.Permission.Group.UserGroup.UserGroup>>(data, count);
            }
        }
    }
}