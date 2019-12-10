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
        /// <param name="powergroupid"></param>
        /// <param name="powergroupname"></param>
        /// <param name="parentid"></param>
        /// <param name="pageindex"></param>
        /// <param name="pagesize"></param>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.MenuInfo.MenuInfo>> GetMenuByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = " m.* ";

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

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            if (pageindex > 0 && pagesize > 0)
            {
                string limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<Domain.Permission.Info.MenuInfo.MenuInfo>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<IEnumerable<Domain.Permission.Info.MenuInfo.MenuInfo>>(data, count);
            }
            else
            {
                var data = Db.Database.SqlQuery<Domain.Permission.Info.MenuInfo.MenuInfo>(string.Format(sql, field));
                return new Pages<IEnumerable<Domain.Permission.Info.MenuInfo.MenuInfo>>(data, count);
            }
        }
    }
}