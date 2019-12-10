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
    public class OperateInfoRepository : Repository<Domain.Permission.Info.OperateInfo.OperateInfo>, IOperateInfoRepository
    {
        public readonly List<OperateInfoTreeOutputDto> Tree;

        public OperateInfoRepository(DbcontextBase context) : base(context)
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
        /// 根据Parent获取子节点  方法1
        /// </summary>
        /// <param name="source"></param>
        /// <param name="node"></param>
        /// <param name="id"></param>
        /// GetParent(DbSet.ToList(), null, input.id);
        private void GetParent(List<Domain.Permission.Info.OperateInfo.OperateInfo> source, OperateInfoTreeOutputDto node, string id)
        {
            List<Domain.Permission.Info.OperateInfo.OperateInfo> list = source.Where(x => x.ParentId == id && x.IsDelete == 0).ToList();
            foreach (var item in list)
            {
                OperateInfoTreeOutputDto model = new OperateInfoTreeOutputDto
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

        /// <summary>
        ///根据权限组Id或名称，遍历关联操作
        /// </summary>
        /// <param name="powergroupid"></param>
        /// <param name="powergroupname"></param>
        /// <param name="parentid"></param>
        /// <param name="pageindex"></param>
        /// <param name="pagesize"></param>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.OperateInfo.OperateInfo>> GetOperateByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = " DISTINCT o.* ";

            string sql = @"SELECT  {0}  FROM
	            OperateInfo AS o
	            INNER JOIN PowerGroupMenuRelation AS pgmr ON o.Id = pgmr.MenuId
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

            int count = Db.Database.Count(string.Format(sql, " count( DISTINCT o.Id ) "));

            if (pageindex > 0 && pagesize > 0)
            {
                string limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<Domain.Permission.Info.OperateInfo.OperateInfo>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<IEnumerable<Domain.Permission.Info.OperateInfo.OperateInfo>>(data, count);
            }
            else
            {
                var data = Db.Database.SqlQuery<Domain.Permission.Info.OperateInfo.OperateInfo>(string.Format(sql, field));
                return new Pages<IEnumerable<Domain.Permission.Info.OperateInfo.OperateInfo>>(data, count);
            }
        }
    }
}