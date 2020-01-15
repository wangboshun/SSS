using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Group.PowerGroup
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerGroupRepository))]
    public class PowerGroupRepository : Repository<SSS.Domain.Permission.Group.PowerGroup.PowerGroup>, IPowerGroupRepository
    {
        private static string field = "pg";

        public PowerGroupRepository(PermissionDbContext context) : base(context)
        {
        }

        /// <summary>
        /// 根据菜单Id或名称，遍历关联权限组
        /// </summary>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Group.PowerGroup.PowerGroup>> GetPowerGroupByMenu(string menuid, string menuname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string sql = @"SELECT  {0}  FROM
	            MenuInfo AS m
	            INNER JOIN PowerGroupMenuRelation AS pgmr ON m.Id = pgmr.MenuId
	            INNER JOIN PowerGroup AS pg ON pgmr.PowerGroupId = pg.Id
            WHERE
	            m.IsDelete = 0
	            AND pg.IsDelete = 0
	            AND pgmr.IsDelete = 0";

            if (!string.IsNullOrWhiteSpace(menuid))
                sql += $" AND m.Id='{menuid}'";

            if (!string.IsNullOrWhiteSpace(menuname))
                sql += $" AND m.MenuName='{menuname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND m.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据操作Id或名称，遍历关联权限组
        /// </summary>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Group.PowerGroup.PowerGroup>> GetPowerGroupByOperate(string operateid, string operatename, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string sql = @"SELECT {0}  FROM
	            OperateInfo AS o
	            INNER JOIN PowerGroupOperateRelation AS pgor ON o.Id = pgor.operateId
	            INNER JOIN PowerGroup AS pg ON pgor.PowerGroupId = pg.Id
            WHERE
	            o.IsDelete = 0
	            AND pg.IsDelete = 0
	            AND pgor.IsDelete = 0";

            if (!string.IsNullOrWhiteSpace(operateid))
                sql += $" AND o.Id='{operateid}'";

            if (!string.IsNullOrWhiteSpace(operatename))
                sql += $" AND o.OperateName='{operatename}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND o.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据权限Id或名称，遍历关联权限组
        /// </summary>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Group.PowerGroup.PowerGroup>> GetPowerGroupByPower(string powerid, string powername, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string sql = @"SELECT {0}  FROM
	                PowerInfo AS p
	                INNER JOIN PowerGroupRelation AS pgr ON p.id=pgr.PowerId
	                INNER JOIN PowerGroup AS pg ON pgr.PowerGroupId=pg.Id
                WHERE
	                p.IsDelete = 0
	                AND pg.IsDelete = 0
	                AND pgr.IsDelete =0 ";

            if (!string.IsNullOrWhiteSpace(powerid))
                sql += $" AND p.Id='{powerid}'";

            if (!string.IsNullOrWhiteSpace(powername))
                sql += $" AND p.PowerName='{powername}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND p.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据角色组Id或名称，遍历关联权限组
        /// </summary>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Group.PowerGroup.PowerGroup>> GetPowerGroupByRoleGroup(string rolegroupid, string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string sql = @"SELECT {0} FROM
	               	PowerGroup AS pg
	                INNER JOIN RoleGroupPowerGroupRelation AS rgpgr ON rgpgr.PowerGroupId = pg.Id
	                INNER JOIN RoleGroup AS rg ON rgpgr.RoleGroupId = rg.Id
                WHERE
	                pg.IsDelete = 0
	                AND rgpgr.IsDelete = 0
	                AND pg.IsDelete = 0";

            if (!string.IsNullOrWhiteSpace(rolegroupid))
                sql += $" AND rg.Id='{rolegroupid}'";

            if (!string.IsNullOrWhiteSpace(rolegroupname))
                sql += $" AND rg.RoleGroupName='{rolegroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND rg.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据用户Id或名称，遍历关联权限组
        /// </summary>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Group.PowerGroup.PowerGroup>> GetPowerGroupByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string sql = @"SELECT {0} FROM
	                UserInfo AS u
	                INNER JOIN UserGroupRelation AS ugr ON u.id = ugr.UserId
	                INNER JOIN UserGroupRoleGroupRelation AS ugrgr ON ugrgr.UserGroupId =ugr.UserGroupId
	                INNER JOIN RoleGroupPowerGroupRelation AS rgpgr ON rgpgr.RoleGroupId = ugrgr.RoleGroupId
	                INNER JOIN PowerGroup AS pg ON pg.Id = rgpgr.PowerGroupId
                WHERE
	                u.IsDelete = 0
	                AND ugr.IsDelete = 0
	                AND ugrgr.IsDelete = 0
	                AND rgpgr.IsDelete = 0
	                AND pg.IsDelete = 0 ";

            if (!string.IsNullOrWhiteSpace(userid))
                sql += $" AND u.Id='{userid}'";

            if (!string.IsNullOrWhiteSpace(username))
                sql += $" AND u.UserName='{username}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND u.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据用户组Id或名称，遍历关联权限组
        /// </summary>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Group.PowerGroup.PowerGroup>> GetPowerGroupByUserGroup(string usergroupid, string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
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

            if (!string.IsNullOrWhiteSpace(usergroupid))
                sql += $" AND ug.Id='{usergroupid}'";

            if (!string.IsNullOrWhiteSpace(usergroupname))
                sql += $" AND ug.UserGroupName='{usergroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND ug.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }
    }
}