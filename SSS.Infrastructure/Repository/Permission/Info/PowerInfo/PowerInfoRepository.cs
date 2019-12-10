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

        public PowerInfoRepository(DbcontextBase context) : base(context)
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
        /// 根据Parent获取子节点  方法1
        /// </summary>
        /// <param name="source"></param>
        /// <param name="node"></param>
        /// <param name="id"></param>
        /// GetParent(DbSet.ToList(), null, input.id);
        private void GetParent(List<Domain.Permission.Info.PowerInfo.PowerInfo> source, PowerInfoTreeOutputDto node, string id)
        {
            List<Domain.Permission.Info.PowerInfo.PowerInfo> list = source.Where(x => x.ParentId == id && x.IsDelete == 0).ToList();
            foreach (var item in list)
            {
                PowerInfoTreeOutputDto model = new PowerInfoTreeOutputDto
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

        /// <summary>
        /// 根据用户Id或名称，遍历关联权限
        /// </summary> 
        /// <returns></returns>
        public Pages<List<Domain.Permission.Info.PowerInfo.PowerInfo>> GetPowerByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = @" p.* ";

            string sql = @" SELECT {0} FROM
	                UserInfo AS u
	                INNER JOIN UserGroupRelation AS ugr ON u.id = ugr.UserId
	                INNER JOIN UserGroupRoleGroupRelation AS rgugr ON rgugr.UserGroupId = ugr.UserGroupId
	                INNER JOIN RoleGroupPowerGroupRelation AS rgpgr ON rgpgr.RoleGroupId = rgugr.RoleGroupId
	                INNER JOIN PowerGroupRelation AS pgr ON rgpgr.PowerGroupId = pgr.PowerGroupId
	                INNER JOIN PowerInfo AS p ON p.Id = pgr.PowerId 
                WHERE
	                u.IsDelete = 0 
	                AND ugr.IsDelete = 0 
	                AND rgugr.IsDelete = 0 
	                AND rgpgr.IsDelete = 0 
	                AND p.IsDelete =0 ";

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
                var data = Db.Database.SqlQuery<Domain.Permission.Info.PowerInfo.PowerInfo>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<List<Domain.Permission.Info.PowerInfo.PowerInfo>>(data.ToList(), count);
            }
            else
            {
                var data = Db.Database.SqlQuery<Domain.Permission.Info.PowerInfo.PowerInfo>(string.Format(sql, field));
                return new Pages<List<Domain.Permission.Info.PowerInfo.PowerInfo>>(data?.ToList(), count);
            }
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联权限
        /// </summary> 
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.PowerInfo.PowerInfo>> GetPowerByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = " p.* ";

            string sql = @"SELECT {0}  FROM
	                PowerInfo AS p
	                INNER JOIN PowerGroupRelation AS ppr ON p.id=ppr.PowerId
	                INNER JOIN PowerGroup AS pg ON ppr.PowerGroupId=pg.Id 
                WHERE
	                p.IsDelete = 0 
	                AND pg.IsDelete = 0 
	                AND ppr.IsDelete =0 ";

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
                var data = Db.Database.SqlQuery<Domain.Permission.Info.PowerInfo.PowerInfo>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<IEnumerable<Domain.Permission.Info.PowerInfo.PowerInfo>>(data, count);
            }
            else
            {
                var data = Db.Database.SqlQuery<Domain.Permission.Info.PowerInfo.PowerInfo>(string.Format(sql, field));
                return new Pages<IEnumerable<Domain.Permission.Info.PowerInfo.PowerInfo>>(data, count);
            }
        }

        /// <summary>
        /// 根据角色组Id或名称，遍历关联权限
        /// </summary>
        /// <param name="rolegroupid"></param>
        /// <param name="rolegroupname"></param>
        /// <param name="parentid"></param>
        /// <param name="pageindex"></param>
        /// <param name="pagesize"></param>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.PowerInfo.PowerInfo>> GetPowerByRoleGroup(string rolegroupid, string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = " p.* ";

            string sql = @"SELECT {0} FROM 
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

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            if (pageindex > 0 && pagesize > 0)
            {
                string limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<Domain.Permission.Info.PowerInfo.PowerInfo>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<IEnumerable<Domain.Permission.Info.PowerInfo.PowerInfo>>(data, count);
            }
            else
            {
                var data = Db.Database.SqlQuery<Domain.Permission.Info.PowerInfo.PowerInfo>(string.Format(sql, field));
                return new Pages<IEnumerable<Domain.Permission.Info.PowerInfo.PowerInfo>>(data, count);
            }
        }
    }
}