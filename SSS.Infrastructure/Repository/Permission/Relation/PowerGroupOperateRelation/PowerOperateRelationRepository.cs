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
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerGroupOperateRelationOutputDto>> GetPowerGroupByOperate(PowerGroupOperateRelationInputDto input)
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

            if (!string.IsNullOrWhiteSpace(input.operateid))
                sql += $" AND m.Id='{input.operateid}'";

            if (!string.IsNullOrWhiteSpace(input.operatename))
                sql += $" AND m.MenuName='{input.operatename}'";

            if (!string.IsNullOrWhiteSpace(input.parentid))
                sql += $" AND m.ParentId='{input.parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            var data = Db.Database.SqlQuery<PowerGroupOperateRelationOutputDto>(string.Format(sql, field));

            if (data != null && input.pagesize > 0)
                return new Pages<List<PowerGroupOperateRelationOutputDto>>(data.Skip(input.pagesize * (input.pageindex > 1 ? input.pageindex - 1 : 0)).Take(input.pagesize).ToList(), count);
            return new Pages<List<PowerGroupOperateRelationOutputDto>>(data?.ToList(), count);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联操作
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerGroupOperateRelationOutputDto>> GetOperateListByPowerGroup(PowerGroupOperateRelationInputDto input)
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

            if (!string.IsNullOrWhiteSpace(input.powergroupid))
                sql += $" AND pg.Id='{input.powergroupid}'";

            if (!string.IsNullOrWhiteSpace(input.powergroupname))
                sql += $" AND pg.PowerGroupName='{input.powergroupname}'";

            if (!string.IsNullOrWhiteSpace(input.parentid))
                sql += $" AND pg.ParentId='{input.parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            var data = Db.Database.SqlQuery<PowerGroupOperateRelationOutputDto>(string.Format(sql, field));

            if (data != null && input.pagesize > 0)
                return new Pages<List<PowerGroupOperateRelationOutputDto>>(data.Skip(input.pagesize * (input.pageindex > 1 ? input.pageindex - 1 : 0)).Take(input.pagesize).ToList(), count);
            return new Pages<List<PowerGroupOperateRelationOutputDto>>(data?.ToList(), count);
        }
    }
}