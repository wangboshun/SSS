using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Info.PowerInfo.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.Info.PowerInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerInfoRepository))]
    public class PowerInfoRepository : Repository<Domain.Permission.Info.PowerInfo.PowerInfo>, IPowerInfoRepository
    {
        public readonly List<PowerInfoTreeOutputDto> Tree;
        private static readonly string field = "p";

        public PowerInfoRepository(PermissionDbContext context) : base(context)
        {
            Tree = new List<PowerInfoTreeOutputDto>();
        }

        /// <summary>
        /// 获取菜单下的所有下级
        /// </summary>
        /// <param name="menuid"></param>
        /// <returns></returns>
        public List<PowerInfoTreeOutputDto> GetChildren(string menuid)
        {
            GetParent(DbSet.ToList(), null, menuid);

            return Tree;
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联权限
        /// </summary>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.PowerInfo.PowerInfo>> GetPowerByPowerGroup(string powergroupid,
            string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            var sql = @"SELECT {0}  FROM
	                PowerInfo AS p
	                INNER JOIN PowerGroupRelation AS pgr ON p.id=pgr.PowerId
	                INNER JOIN PowerGroup AS pg ON pgr.PowerGroupId=pg.Id
                WHERE
	                p.IsDelete = 0
	                AND pg.IsDelete = 0
	                AND pgr.IsDelete =0 ";

            if (!string.IsNullOrWhiteSpace(powergroupid))
                sql += $" AND pg.Id='{powergroupid}'";

            if (!string.IsNullOrWhiteSpace(powergroupname))
                sql += $" AND pg.PowerGroupName='{powergroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND pg.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据角色组Id或名称，遍历关联权限
        /// </summary>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.PowerInfo.PowerInfo>> GetPowerByRoleGroup(string rolegroupid,
            string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            var sql = @"SELECT {0} FROM
                    RoleGroup AS rg
	                INNER JOIN RoleGroupPowerGroupRelation AS rgpgr ON rg.Id = rgpgr.RoleGroupId
	                INNER JOIN PowerGroupRelation AS pgr ON pgr.PowerGroupId = rgpgr.PowerGroupId
	                INNER JOIN PowerInfo AS p ON p.Id = pgr.PowerId
                WHERE
	                rg.IsDelete = 0
	                AND rgpgr.IsDelete = 0
	                AND pgr.IsDelete=0
	                AND p.IsDelete=0 ";

            if (!string.IsNullOrWhiteSpace(rolegroupid))
                sql += $" AND rg.Id='{rolegroupid}'";

            if (!string.IsNullOrWhiteSpace(rolegroupname))
                sql += $" AND rg.RoleGroupName='{rolegroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND rg.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据用户Id或名称，遍历关联权限
        /// </summary>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.PowerInfo.PowerInfo>> GetPowerByUser(string userid,
            string username, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            var sql = @" SELECT {0} FROM
	                UserInfo AS u
	                INNER JOIN UserGroupRelation AS ugr ON u.id = ugr.UserId
	                INNER JOIN UserGroupRoleGroupRelation AS ugrgr ON ugrgr.UserGroupId = ugr.UserGroupId
	                INNER JOIN RoleGroupPowerGroupRelation AS rgpgr ON rgpgr.RoleGroupId = ugrgr.RoleGroupId
	                INNER JOIN PowerGroupRelation AS pgr ON rgpgr.PowerGroupId = pgr.PowerGroupId
	                INNER JOIN PowerInfo AS p ON p.Id = pgr.PowerId
                WHERE
	                u.IsDelete = 0
                    AND pgr.IsDelete=0
	                AND ugr.IsDelete = 0
	                AND ugrgr.IsDelete = 0
	                AND rgpgr.IsDelete = 0
	                AND p.IsDelete =0 ";

            if (!string.IsNullOrWhiteSpace(userid))
                sql += $" AND u.Id='{userid}'";

            if (!string.IsNullOrWhiteSpace(username))
                sql += $" AND u.UserName='{username}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND u.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据用户组Id或名称，遍历关联权限
        /// </summary>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.PowerInfo.PowerInfo>> GetPowerByUserGroup(string usergroupid,
            string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            var sql = @" SELECT {0} FROM
	                PowerInfo AS p
	                INNER JOIN PowerGroupRelation AS pgr ON p.Id = pgr.PowerId
	                INNER JOIN RoleGroupPowerGroupRelation AS rgpgr ON rgpgr.PowerGroupId = pgr.PowerGroupId
	                INNER JOIN UserGroupRoleGroupRelation AS ugrgr ON rgpgr.RoleGroupId = ugrgr.RoleGroupId
	                INNER JOIN UserGroup AS ug ON ugrgr.UserGroupId = ug.Id
                WHERE
	                p.IsDelete = 0
	                AND pgr.IsDelete = 0
	                AND ugrgr.IsDelete = 0
	                AND rgpgr.IsDelete = 0
	                AND ug.IsDelete =0 ";

            if (!string.IsNullOrWhiteSpace(usergroupid))
                sql += $" AND ug.Id='{usergroupid}'";

            if (!string.IsNullOrWhiteSpace(usergroupname))
                sql += $" AND ug.UserGroupName='{usergroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND ug.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据Parent获取子节点 方法1
        /// </summary>
        /// <param name="source"></param>
        /// <param name="node"></param>
        /// <param name="id"></param>
        /// GetParent(DbSet.ToList(), null, input.id);
        private void GetParent(List<Domain.Permission.Info.PowerInfo.PowerInfo> source, PowerInfoTreeOutputDto node,
            string id)
        {
            var list = source.Where(x => x.ParentId == id && x.IsDelete == 0).ToList();
            foreach (var item in list)
            {
                var model = new PowerInfoTreeOutputDto
                {
                    id = item.Id,
                    createtime = item.CreateTime,
                    powername = item.PowerName,
                    parentid = item.ParentId
                };

                GetParent(source, model, item.Id);

                if (node == null)
                    Tree.Add(model);
                else
                    node.Item.Add(model);
            }
        }
    }
}