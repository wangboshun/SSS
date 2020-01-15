using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Info.OperateInfo;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.Info.OperateInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(IOperateInfoRepository))]
    public class OperateInfoRepository : Repository<Domain.Permission.Info.OperateInfo.OperateInfo>,
        IOperateInfoRepository
    {
        public readonly List<OperateInfoTreeOutputDto> Tree;
        private static readonly string field = "o";

        public OperateInfoRepository(PermissionDbContext context) : base(context)
        {
            Tree = new List<OperateInfoTreeOutputDto>();
        }

        /// <summary>
        /// 获取操作下的所有下级
        /// </summary>
        /// <param name="operateid"></param>
        /// <returns></returns>
        public List<OperateInfoTreeOutputDto> GetChildrenById(string operateid)
        {
            GetParent(DbSet.ToList(), null, operateid);

            return Tree;
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联操作
        /// </summary>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.OperateInfo.OperateInfo>> GetOperateByPowerGroup(
            string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            var sql = @"SELECT  {0}  FROM
	           	OperateInfo AS o
	            INNER JOIN PowerGroupOperateRelation AS pgor ON o.Id = pgor.OperateId
	            INNER JOIN PowerGroup AS pg ON pgor.PowerGroupId = pg.Id
            WHERE
	            o.IsDelete = 0
	            AND pg.IsDelete = 0
	            AND pgor.IsDelete = 0";

            if (!string.IsNullOrWhiteSpace(powergroupid))
                sql += $" AND pg.Id='{powergroupid}'";

            if (!string.IsNullOrWhiteSpace(powergroupname))
                sql += $" AND pg.PowerGroupName='{powergroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND pg.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据角色组Id或名称，遍历关联操作
        /// </summary>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.OperateInfo.OperateInfo>> GetOperateByRoleGroup(
            string rolegroupid, string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            var sql = @"SELECT  {0}  FROM
	                RoleGroup AS rg
	                INNER JOIN RoleGroupPowerGroupRelation AS rgpgr ON rgpgr.RoleGroupId = rg.Id
	                INNER JOIN PowerGroupOperateRelation AS pgopr ON pgopr.PowerGroupId = rgpgr.PowerGroupId
	                INNER JOIN OperateInfo AS o ON pgopr.OperateId = o.Id
                WHERE
	                rg.IsDelete = 0
	                AND rgpgr.IsDelete = 0
	                AND pgopr.IsDelete = 0
	                AND o.IsDelete = 0";

            if (!string.IsNullOrWhiteSpace(rolegroupid))
                sql += $" AND rg.Id='{rolegroupid}'";

            if (!string.IsNullOrWhiteSpace(rolegroupname))
                sql += $" AND rg.RoleGroupName='{rolegroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND rg.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据用户Id或名称，遍历关联操作
        /// </summary>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.OperateInfo.OperateInfo>> GetOperateByUser(string userid,
            string username, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            var sql = @"SELECT  {0}  FROM
	           		UserInfo AS u
	                INNER JOIN UserGroupRelation AS ugr ON u.id = ugr.UserId
	                INNER JOIN UserGroupRoleGroupRelation AS ugrgr ON ugrgr.UserGroupId = ugr.UserGroupId
	                INNER JOIN RoleGroupPowerGroupRelation AS rgpgr ON rgpgr.RoleGroupId = ugrgr.RoleGroupId
	                INNER JOIN PowerGroupOperateRelation AS pgopr ON pgopr.PowerGroupId = rgpgr.PowerGroupId
	                INNER JOIN OperateInfo AS o ON pgopr.OperateId = o.Id
                WHERE
	                u.IsDelete = 0
	                AND ugr.IsDelete = 0
	                AND ugrgr.IsDelete = 0
	                AND rgpgr.IsDelete = 0
	                AND pgopr.IsDelete = 0
	                AND o.IsDelete = 0";

            if (!string.IsNullOrWhiteSpace(userid))
                sql += $" AND u.Id='{userid}'";

            if (!string.IsNullOrWhiteSpace(username))
                sql += $" AND u.UserName='{username}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND u.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据用户组Id或名称，遍历关联操作
        /// </summary>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.OperateInfo.OperateInfo>> GetOperateByUserGroup(
            string usergroupid, string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            var sql = @"SELECT  {0}  FROM
	                UserGroup AS ug
	                INNER JOIN UserGroupRelation AS ugr ON ug.id = ugr.UserGroupId
	                INNER JOIN UserGroupRoleGroupRelation AS ugrgr ON ugrgr.UserGroupId = ugr.UserGroupId
	                INNER JOIN RoleGroupPowerGroupRelation AS rgpgr ON rgpgr.RoleGroupId = ugrgr.RoleGroupId
	                INNER JOIN PowerGroupOperateRelation AS pgopr ON pgopr.PowerGroupId = rgpgr.PowerGroupId
	                INNER JOIN OperateInfo AS o ON pgopr.OperateId = o.Id
                WHERE
	                ug.IsDelete = 0
	                AND ugr.IsDelete = 0
	                AND ugrgr.IsDelete = 0
	                AND rgpgr.IsDelete = 0
	                AND pgopr.IsDelete = 0
	                AND o.IsDelete = 0";

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
        private void GetParent(List<Domain.Permission.Info.OperateInfo.OperateInfo> source,
            OperateInfoTreeOutputDto node, string id)
        {
            var list = source.Where(x => x.ParentId == id && x.IsDelete == 0).ToList();
            foreach (var item in list)
            {
                var model = new OperateInfoTreeOutputDto
                {
                    id = item.Id,
                    createtime = item.CreateTime,
                    operatename = item.OperateName,
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