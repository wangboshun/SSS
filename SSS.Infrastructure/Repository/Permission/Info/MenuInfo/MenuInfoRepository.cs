using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Info.MenuInfo.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.Info.MenuInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(IMenuInfoRepository))]
    public class MenuInfoRepository : Repository<Domain.Permission.Info.MenuInfo.MenuInfo>, IMenuInfoRepository
    {
        private static string field = "m";

        public readonly List<MenuInfoTreeOutputDto> Tree;

        public MenuInfoRepository(DbcontextBase context) : base(context)
        {
            Tree = new List<MenuInfoTreeOutputDto>();
        }

        /// <summary>
        /// 获取菜单下的所有下级
        /// </summary>
        /// <param name="menuid"></param>
        /// <returns></returns>
        public List<MenuInfoTreeOutputDto> GetChildren(string menuid)
        {
            GetParent(DbSet.ToList(), null, menuid);

            return Tree;
        }

        /// <summary>
        /// 根据Parent获取子节点  方法1
        /// </summary>
        /// <param name="source"></param>
        /// <param name="node"></param>
        /// <param name="id"></param>
        /// GetParent(DbSet.ToList(), null, input.id);
        private void GetParent(List<Domain.Permission.Info.MenuInfo.MenuInfo> source, MenuInfoTreeOutputDto node, string id)
        {
            List<Domain.Permission.Info.MenuInfo.MenuInfo> list = source.Where(x => x.ParentId == id && x.IsDelete == 0).ToList();
            foreach (var item in list)
            {
                MenuInfoTreeOutputDto model = new MenuInfoTreeOutputDto
                {
                    id = item.Id,
                    createtime = item.CreateTime,
                    menuname = item.MenuName,
                    parentid = item.ParentId
                };

                GetParent(source, model, item.Id);

                if (node == null)
                    Tree.Add(model);
                else
                    node.Item.Add(model);
            }
        }

        /// <summary>
        ///根据权限组Id或名称，遍历关联菜单
        /// </summary> 
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.MenuInfo.MenuInfo>> GetMenuByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string sql = @"SELECT  {0}  FROM
	            MenuInfo AS m
	            INNER JOIN PowerGroupMenuRelation AS pgmr ON m.Id = pgmr.MenuId
	            INNER JOIN PowerGroup AS pg ON pgmr.PowerGroupId = pg.Id 
            WHERE
	            m.IsDelete = 0 
	            AND pg.IsDelete = 0 
	            AND pgmr.IsDelete = 0";

            if (!string.IsNullOrWhiteSpace(powergroupid))
                sql += $" AND pg.Id='{powergroupid}'";

            if (!string.IsNullOrWhiteSpace(powergroupname))
                sql += $" AND pg.PowerGroupName='{powergroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND pg.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据用户Id或名称，遍历关联菜单
        /// </summary> 
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.MenuInfo.MenuInfo>> GetMenuByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string sql = @"SELECT  {0}  FROM
	           		UserInfo AS u
	                INNER JOIN UserGroupRelation AS ugr ON u.id = ugr.UserId
	                INNER JOIN UserGroupRoleGroupRelation AS rgugr ON rgugr.UserGroupId = ugr.UserGroupId
	                INNER JOIN RoleGroupPowerGroupRelation AS rgpgr ON rgpgr.RoleGroupId = rgugr.RoleGroupId
	                INNER JOIN PowerGroupMenuRelation AS pgomr ON pgomr.PowerGroupId = rgpgr.PowerGroupId
	                INNER JOIN MenuInfo AS m ON pgomr.MenuId = m.Id 
                WHERE
	                u.IsDelete = 0 
	                AND ugr.IsDelete = 0 
	                AND rgugr.IsDelete = 0 
	                AND rgpgr.IsDelete = 0 
	                AND pgomr.IsDelete = 0 
	                AND m.IsDelete = 0";

            if (!string.IsNullOrWhiteSpace(userid))
                sql += $" AND u.Id='{userid}'";

            if (!string.IsNullOrWhiteSpace(username))
                sql += $" AND u.UserName='{username}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND u.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据用户组Id或名称，遍历关联菜单
        /// </summary> 
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.MenuInfo.MenuInfo>> GetMenuByUserGroup(string usergroupid, string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string sql = @"SELECT  {0}  FROM
	                UserGroup AS ug
	                INNER JOIN UserGroupRelation AS ugr ON ugr.UserGroupId = ug.id
	                INNER JOIN UserGroupRoleGroupRelation AS rgugr ON rgugr.UserGroupId = ugr.UserGroupId
	                INNER JOIN RoleGroupPowerGroupRelation AS rgpgr ON rgpgr.RoleGroupId = rgugr.RoleGroupId
	                INNER JOIN PowerGroupMenuRelation AS pgomr ON pgomr.PowerGroupId = rgpgr.PowerGroupId
	                INNER JOIN MenuInfo AS m ON pgomr.MenuId = m.Id 
                WHERE
	                ug.IsDelete = 0 
	                AND ugr.IsDelete = 0 
	                AND rgugr.IsDelete = 0 
	                AND rgpgr.IsDelete = 0 
	                AND pgomr.IsDelete = 0 
	                AND m.IsDelete = 0";

            if (!string.IsNullOrWhiteSpace(usergroupid))
                sql += $" AND ug.Id='{usergroupid}'";

            if (!string.IsNullOrWhiteSpace(usergroupname))
                sql += $" AND ug.UserGroupName='{usergroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND ug.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据角色组Id或名称，遍历关联菜单
        /// </summary> 
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.MenuInfo.MenuInfo>> GetMenuByRoleGroup(string rolegroupid, string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string sql = @"SELECT  {0}  FROM
	                RoleGroup AS rg
	                INNER JOIN RoleGroupPowerGroupRelation AS rgpgr ON rgpgr.RoleGroupId = rg.Id
	                INNER JOIN PowerGroupMenuRelation AS pgomr ON pgomr.PowerGroupId = rgpgr.PowerGroupId
	                INNER JOIN MenuInfo AS m ON pgomr.MenuId = m.Id 
                WHERE
	                rg.IsDelete = 0 
	                AND rgpgr.IsDelete = 0 
	                AND pgomr.IsDelete = 0 
	                AND m.IsDelete = 0";

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