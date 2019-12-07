using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.PowerGroupOperateRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.Relation.PowerGroupOperateRelation
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerGroupOperateRelationRepository))]
    public class PowerGroupOperateRelationRepository : Repository<Domain.Permission.Relation.PowerGroupOperateRelation.PowerGroupOperateRelation>, IPowerGroupOperateRelationRepository
    {
        public PowerGroupOperateRelationRepository(DbcontextBase context) : base(context)
        {
        }

        /// <summary>
        ///  根据操作Id或名称，遍历关联权限组
        /// </summary> 
        /// <returns></returns>
        public Pages<List<PowerGroupOperateRelationOutputDto>> GetPowerGroupByOperate(string operateid, string operatename, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = @" o.Id AS operateid,
	            o.operateName AS operatename,
	            pg.PowerGroupName AS powergroupname,
	            pg.Id AS powergroupid,
                pgor.CreateTime as createtime,
	            pgor.Id AS id ";

            string sql = @"SELECT {0} 
            FROM
	            OperateInfo AS o
	            INNER JOIN PowerGroupOperateRelation AS pgor ON o.Id = pgor.operateId
	            INNER JOIN PowerGroup AS pg ON pgor.PowerGroupId = pg.Id 
            WHERE
	            o.IsDelete = 0 
	            AND pg.IsDelete = 0 
	            AND pgor.IsDelete = 0";

            if (!string.IsNullOrWhiteSpace(operateid))
                sql += $" AND m.Id='{operateid}'";

            if (!string.IsNullOrWhiteSpace(operatename))
                sql += $" AND m.MenuName='{operatename}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND m.ParentId='{parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            if (pageindex > 0 && pagesize > 0)
            {
                string limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<PowerGroupOperateRelationOutputDto>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<List<PowerGroupOperateRelationOutputDto>>(data.ToList(), count);
            }
            else
            {
                var data = Db.Database.SqlQuery<PowerGroupOperateRelationOutputDto>(string.Format(sql, field));
                return new Pages<List<PowerGroupOperateRelationOutputDto>>(data?.ToList(), count);
            }
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联操作
        /// </summary> 
        /// <returns></returns>
        public Pages<List<PowerGroupOperateRelationOutputDto>> GetOperateByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = @" o.Id AS operateid,
	            o.operateName AS operatename,
	            pg.PowerGroupName AS powergroupname,
	            pg.Id AS powergroupid,
                pgor.CreateTime as createtime,
	            pgor.Id AS id ";

            string sql = @"SELECT {0} 
            FROM
	            OperateInfo AS o
	            INNER JOIN PowerGroupOperateRelation AS pgor ON o.Id = pgor.operateId
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

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            if (pageindex > 0 && pagesize > 0)
            {
                string limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<PowerGroupOperateRelationOutputDto>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<List<PowerGroupOperateRelationOutputDto>>(data.ToList(), count);
            }
            else
            {
                var data = Db.Database.SqlQuery<PowerGroupOperateRelationOutputDto>(string.Format(sql, field));
                return new Pages<List<PowerGroupOperateRelationOutputDto>>(data?.ToList(), count);
            }
        }
    }
}