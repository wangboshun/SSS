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
        public readonly List<RoleInfoTreeOutputDto> Tree;

        public RoleInfoRepository(DbcontextBase context) : base(context)
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
            string field = " r.* ";

            string sql = @"SELECT {0}   FROM
	                RoleInfo AS r
	                INNER JOIN RoleGroupRelation AS rrr ON r.id = rrr.RoleId
	                INNER JOIN RoleGroup AS rg ON rrr.RoleGroupId = rg.Id 
                WHERE
	   	            r.IsDelete=0 
	                AND rg.IsDelete=0 
	                AND rrr.IsDelete=0 ";

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
                var data = Db.Database.SqlQuery<Domain.Permission.Info.RoleInfo.RoleInfo>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<IEnumerable<Domain.Permission.Info.RoleInfo.RoleInfo>>(data, count);
            }
            else
            {
                var data = Db.Database.SqlQuery<Domain.Permission.Info.RoleInfo.RoleInfo>(string.Format(sql, field));
                return new Pages<IEnumerable<Domain.Permission.Info.RoleInfo.RoleInfo>>(data, count);
            }
        }
    }
}