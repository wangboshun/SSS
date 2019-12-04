using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.PowerGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.Relation.PowerGroupRelation
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerGroupRelationRepository))]
    public class PowerGroupRelationRepository : Repository<Domain.Permission.Relation.PowerGroupRelation.PowerGroupRelation>, IPowerGroupRelationRepository
    {
        public PowerGroupRelationRepository(DbcontextBase context) : base(context)
        {
        }

        /// <summary>
        /// 根据权限Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerGroupRelationOutputDto>> GetPowerGroupByPower(PowerGroupRelationInputDto input)
        {
            string field = @"p.id AS 'powerid',
	                p.PowerName AS 'powername',
	                pg.PowerGroupName AS 'powergroupname',
	                pg.id AS 'powergroupid',
	                ppr.id AS 'id'";

            string sql = @"SELECT {0}
                                FROM
	                PowerInfo AS p
	                INNER JOIN PowerGroupRelation AS ppr ON p.id=ppr.PowerId
	                INNER JOIN PowerGroup AS pg ON ppr.PowerGroupId=pg.Id 
                WHERE
	                p.IsDelete = 0 
	                AND pg.IsDelete = 0 
	                AND ppr.IsDelete =0 ";

            if (!string.IsNullOrWhiteSpace(input.powerid))
                sql += $" AND p.Id='{input.powerid}'";

            if (!string.IsNullOrWhiteSpace(input.powername))
                sql += $" AND p.PowerName='{input.powername}'";

            if (!string.IsNullOrWhiteSpace(input.parentid))
                sql += $" AND p.ParentId='{input.parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            var data = Db.Database.SqlQuery<PowerGroupRelationOutputDto>(string.Format(sql, field));

            if (data != null && input.pagesize > 0)
                return new Pages<List<PowerGroupRelationOutputDto>>(data.Skip(input.pagesize * (input.pageindex > 1 ? input.pageindex - 1 : 0)).Take(input.pagesize).ToList(), count);
            return new Pages<List<PowerGroupRelationOutputDto>>(data?.ToList(), count);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联权限
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerGroupRelationOutputDto>> GetPowerListByGroup(PowerGroupRelationInputDto input)
        {
            string field = @"p.id AS 'powerid',
	                p.PowerName AS 'powername',
	                pg.PowerGroupName AS 'powergroupname',
	                pg.id AS 'powergroupid',
	                ppr.id AS 'id'";

            string sql = @"SELECT {0}
                                FROM
	                PowerInfo AS p
	                INNER JOIN PowerGroupRelation AS ppr ON p.id=ppr.PowerId
	                INNER JOIN PowerGroup AS pg ON ppr.PowerGroupId=pg.Id 
                WHERE
	                p.IsDelete = 0 
	                AND pg.IsDelete = 0 
	                AND ppr.IsDelete =0 ";

            if (!string.IsNullOrWhiteSpace(input.powergroupid))
                sql += $" AND pg.Id='{input.powergroupid}'";

            if (!string.IsNullOrWhiteSpace(input.powergroupname))
                sql += $" AND pg.PowerGroupName='{input.powergroupname}'";

            if (!string.IsNullOrWhiteSpace(input.parentid))
                sql += $" AND pg.ParentId='{input.parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            var data = Db.Database.SqlQuery<PowerGroupRelationOutputDto>(string.Format(sql, field));

            if (data != null && input.pagesize > 0)
                return new Pages<List<PowerGroupRelationOutputDto>>(data.Skip(input.pagesize * (input.pageindex > 1 ? input.pageindex - 1 : 0)).Take(input.pagesize).ToList(), count);
            return new Pages<List<PowerGroupRelationOutputDto>>(data?.ToList(), count);
        }
    }
}