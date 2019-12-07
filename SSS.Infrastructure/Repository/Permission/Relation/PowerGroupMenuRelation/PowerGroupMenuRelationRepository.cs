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
        /// <returns></returns>
        public Pages<List<PowerGroupMenuRelationOutputDto>> GetPowerGroupByMenu(string menuid, string menuname, string parentid = "", int pageindex = 0, int pagesize = 0)
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

            if (!string.IsNullOrWhiteSpace(menuid))
                sql += $" AND m.Id='{menuid}'";

            if (!string.IsNullOrWhiteSpace(menuname))
                sql += $" AND m.MenuName='{menuname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND m.ParentId='{parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            if (pageindex > 0 && pagesize > 0)
            {
                string limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<PowerGroupMenuRelationOutputDto>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<List<PowerGroupMenuRelationOutputDto>>(data.ToList(), count);
            }
            else
            {
                var data = Db.Database.SqlQuery<PowerGroupMenuRelationOutputDto>(string.Format(sql, field));
                return new Pages<List<PowerGroupMenuRelationOutputDto>>(data?.ToList(), count);
            }
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联菜单
        /// </summary> 
        /// <returns></returns>
        public Pages<List<PowerGroupMenuRelationOutputDto>> GetMenuByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
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
                var data = Db.Database.SqlQuery<PowerGroupMenuRelationOutputDto>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<List<PowerGroupMenuRelationOutputDto>>(data.ToList(), count);
            }
            else
            {
                var data = Db.Database.SqlQuery<PowerGroupMenuRelationOutputDto>(string.Format(sql, field));
                return new Pages<List<PowerGroupMenuRelationOutputDto>>(data?.ToList(), count);
            }
        }
    }
}