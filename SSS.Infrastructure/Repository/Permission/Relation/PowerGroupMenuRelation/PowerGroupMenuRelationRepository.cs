using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.PowerGroupMenuRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.Relation.PowerGroupMenuRelation
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerGroupMenuRelationRepository))]
    public class PowerGroupMenuRelationRepository : Repository<Domain.Permission.Relation.PowerGroupMenuRelation.PowerGroupMenuRelation>, IPowerGroupMenuRelationRepository
    {
        public PowerGroupMenuRelationRepository(DbcontextBase context) : base(context)
        {
        }

        /// <summary>
        /// 根据菜单Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerGroupMenuRelationOutputDto>> GetPowerGroupByMenu(PowerGroupMenuRelationInputDto input)
        {
            string field = @"m.Id AS menuid,
	            m.MenuUrl AS menuurl,
	            m.MenuName AS menuname,
	            pg.PowerGroupName AS powergroupname,
	            pg.Id AS powergroupid,
                pgmr.CreateTime as createtime,
	            pgmr.Id AS id ";

            string sql = @"SELECT  {0}
            FROM
	            MenuInfo AS m
	            INNER JOIN PowerGroupMenuRelation AS pgmr ON m.Id = pgmr.MenuId
	            INNER JOIN PowerGroup AS pg ON pgmr.PowerGroupId = pg.Id 
            WHERE
	            m.IsDelete = 0 
	            AND pg.IsDelete = 0 
	            AND pgmr.IsDelete = 0";

            if (!string.IsNullOrWhiteSpace(input.menuid))
                sql += $" AND m.Id='{input.menuid}'";

            if (!string.IsNullOrWhiteSpace(input.menuname))
                sql += $" AND m.MenuName='{input.menuname}'";

            if (!string.IsNullOrWhiteSpace(input.parentid))
                sql += $" AND m.ParentId='{input.parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            var data = Db.Database.SqlQuery<PowerGroupMenuRelationOutputDto>(string.Format(sql, field));

            if (data != null && input.pagesize > 0)
                return new Pages<List<PowerGroupMenuRelationOutputDto>>(data.Skip(input.pagesize * (input.pageindex > 1 ? input.pageindex - 1 : 0)).Take(input.pagesize).ToList(), count);
            return new Pages<List<PowerGroupMenuRelationOutputDto>>(data?.ToList(), count);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联菜单
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerGroupMenuRelationOutputDto>> GetMenuByPowerGroup(PowerGroupMenuRelationInputDto input)
        {
            string field = @"m.Id AS menuid,
	            m.MenuUrl AS menuurl,
	            m.MenuName AS menuname,
	            pg.PowerGroupName AS powergroupname,
	            pg.Id AS powergroupid,
                pgmr.CreateTime as createtime,
	            pgmr.Id AS id ";

            string sql = @"SELECT  {0}
            FROM
	            MenuInfo AS m
	            INNER JOIN PowerGroupMenuRelation AS pgmr ON m.Id = pgmr.MenuId
	            INNER JOIN PowerGroup AS pg ON pgmr.PowerGroupId = pg.Id 
            WHERE
	            m.IsDelete = 0 
	            AND pg.IsDelete = 0 
	            AND pgmr.IsDelete = 0";

            if (!string.IsNullOrWhiteSpace(input.powergroupid))
                sql += $" AND pg.Id='{input.powergroupid}'";

            if (!string.IsNullOrWhiteSpace(input.powergroupname))
                sql += $" AND pg.PowerGroupName='{input.powergroupname}'";

            if (!string.IsNullOrWhiteSpace(input.parentid))
                sql += $" AND pg.ParentId='{input.parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            var data = Db.Database.SqlQuery<PowerGroupMenuRelationOutputDto>(string.Format(sql, field));

            if (data != null && input.pagesize > 0)
                return new Pages<List<PowerGroupMenuRelationOutputDto>>(data.Skip(input.pagesize * (input.pageindex > 1 ? input.pageindex - 1 : 0)).Take(input.pagesize).ToList(), count);
            return new Pages<List<PowerGroupMenuRelationOutputDto>>(data?.ToList(), count);
        }
    }
}