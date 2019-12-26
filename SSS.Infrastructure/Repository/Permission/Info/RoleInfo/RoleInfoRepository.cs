using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Info.RoleInfo.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.Info.RoleInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleInfoRepository))]
    public class RoleInfoRepository : Repository<Domain.Permission.Info.RoleInfo.RoleInfo>, IRoleInfoRepository
    {
        private static string field = "r";

        public readonly List<RoleInfoTreeOutputDto> Tree;

        public RoleInfoRepository(SystemDbContext context) : base(context)
        {
            Tree = new List<RoleInfoTreeOutputDto>();
        }

        /// <summary>
        /// 获取角色下的所有下级
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        public List<RoleInfoTreeOutputDto> GetChildren(string roleid)
        {
            GetParent(DbSet.ToList(), null, roleid);

            return Tree;
        }

        /// <summary>
        /// 根据Parent获取子节点 
        /// </summary>
        /// <param name="source"></param>
        /// <param name="node"></param>
        /// <param name="id"></param>
        /// GetParent(DbSet.ToList(), null, input.id);
        private void GetParent(List<Domain.Permission.Info.RoleInfo.RoleInfo> source, RoleInfoTreeOutputDto node, string id)
        {
            List<Domain.Permission.Info.RoleInfo.RoleInfo> list = source.Where(x => x.ParentId == id && x.IsDelete == 0).ToList();
            foreach (var item in list)
            {
                RoleInfoTreeOutputDto model = new RoleInfoTreeOutputDto
                {
                    id = item.Id,
                    createtime = item.CreateTime,
                    rolename = item.RoleName,
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
        /// 根据角色组Id或名称，遍历关联角色
        /// </summary> 
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.RoleInfo.RoleInfo>> GetRoleByRoleGroup(string rolegroupid, string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string sql = @"SELECT {0}   FROM
	                RoleInfo AS r
	                INNER JOIN RoleGroupRelation AS rgr ON r.id = rgr.RoleId
	                INNER JOIN RoleGroup AS rg ON rgr.RoleGroupId = rg.Id 
                WHERE
	   	            r.IsDelete=0 
	                AND rg.IsDelete=0 
	                AND rgr.IsDelete=0 ";

            if (!string.IsNullOrWhiteSpace(rolegroupid))
                sql += $" AND rg.Id='{rolegroupid}'";

            if (!string.IsNullOrWhiteSpace(rolegroupname))
                sql += $" AND rg.RoleGroupName='{rolegroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND rg.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联角色
        /// </summary> 
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.RoleInfo.RoleInfo>> GetRoleByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string sql = @"SELECT {0}   FROM
	                RoleInfo AS r
	                INNER JOIN RoleGroupRelation AS rgr ON r.Id = rgr.RoleId 
	                INNER JOIN RoleGroupPowerGroupRelation AS rgpgr ON rgpgr.RoleGroupId = rgr.RoleGroupId
	                INNER JOIN PowerGroup AS pg ON pg.Id = rgpgr.PowerGroupId
                WHERE
	                r.IsDelete = 0 
	                AND rgr.IsDelete = 0  
	                AND rgpgr.IsDelete = 0 
                    AND pg.IsDelete=0 ";

            if (!string.IsNullOrWhiteSpace(powergroupid))
                sql += $" AND pg.Id='{powergroupid}'";

            if (!string.IsNullOrWhiteSpace(powergroupname))
                sql += $" AND pg.PowerGroupName='{powergroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND pg.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据用户组Id或名称，遍历关联角色
        /// </summary> 
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.RoleInfo.RoleInfo>> GetRoleByUserGroup(string usergroupid, string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string sql = @"SELECT {0}   FROM
	                RoleInfo AS r
	                INNER JOIN RoleGroupRelation AS rgr ON r.Id = rgr.RoleId
	                INNER JOIN UserGroupRoleGroupRelation AS ugrgr ON ugrgr.RoleGroupId = rgr.RoleGroupId
	                INNER JOIN UserGroup AS ug ON ug.Id = ugrgr.UserGroupId 
                WHERE
	                r.IsDelete = 0 
	                AND ug.IsDelete = 0 
	                AND rgr.IsDelete = 0 
	                AND ugrgr.IsDelete = 0 ";

            if (!string.IsNullOrWhiteSpace(usergroupid))
                sql += $" AND ug.Id='{usergroupid}'";

            if (!string.IsNullOrWhiteSpace(usergroupname))
                sql += $" AND ug.UserGroupName='{usergroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND ug.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        ///  根据用户Id或名称，遍历关联角色
        /// </summary> 
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.RoleInfo.RoleInfo>> GetRoleByUser(string userid, string usename, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string sql = @"SELECT {0}   FROM
	                RoleInfo AS r
	                INNER JOIN RoleGroupRelation AS rgr ON r.Id = rgr.RoleId
	                INNER JOIN UserGroupRoleGroupRelation AS ugrgr ON ugrgr.RoleGroupId = rgr.RoleGroupId
	                INNER JOIN UserGroup AS ug ON ug.Id = ugrgr.UserGroupId
	                INNER JOIN UserGroupRelation AS ugr ON ugr.UserGroupId = ug.Id
	                INNER JOIN UserInfo AS u ON u.Id = ugr.UserId 
                WHERE
	                r.IsDelete = 0 
	                AND u.IsDelete = 0 
	                AND ug.IsDelete = 0 
	                AND rgr.IsDelete = 0 
	                AND ugrgr.IsDelete = 0 ";

            if (!string.IsNullOrWhiteSpace(userid))
                sql += $" AND u.Id='{userid}'";

            if (!string.IsNullOrWhiteSpace(usename))
                sql += $" AND u.UserName='{usename}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND u.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }
    }
}