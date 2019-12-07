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
        /// <returns></returns>
        public Pages<List<PowerGroupRelationOutputDto>> GetPowerGroupByPower(string powerid, string powername, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = @"p.id AS 'powerid',
	                p.PowerName AS 'powername',
	                pg.PowerGroupName AS 'powergroupname',
	                pg.id AS 'powergroupid',
                    ppr.CreateTime as createtime,
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

            if (!string.IsNullOrWhiteSpace(powerid))
                sql += $" AND p.Id='{powerid}'";

            if (!string.IsNullOrWhiteSpace(powername))
                sql += $" AND p.PowerName='{powername}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND p.ParentId='{parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            if (pageindex > 0 && pagesize > 0)
            {
                string limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<PowerGroupRelationOutputDto>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<List<PowerGroupRelationOutputDto>>(data.ToList(), count);
            }
            else
            {
                var data = Db.Database.SqlQuery<PowerGroupRelationOutputDto>(string.Format(sql, field));
                return new Pages<List<PowerGroupRelationOutputDto>>(data?.ToList(), count);
            }
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联权限
        /// </summary> 
        /// <returns></returns>
        public Pages<List<PowerGroupRelationOutputDto>> GetPowerByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = @"p.id AS 'powerid',
	                p.PowerName AS 'powername',
	                pg.PowerGroupName AS 'powergroupname',
	                pg.id AS 'powergroupid',
                    ppr.CreateTime as createtime,
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
                var data = Db.Database.SqlQuery<PowerGroupRelationOutputDto>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<List<PowerGroupRelationOutputDto>>(data.ToList(), count);
            }
            else
            {
                var data = Db.Database.SqlQuery<PowerGroupRelationOutputDto>(string.Format(sql, field));
                return new Pages<List<PowerGroupRelationOutputDto>>(data?.ToList(), count);
            }
        }
    }
}