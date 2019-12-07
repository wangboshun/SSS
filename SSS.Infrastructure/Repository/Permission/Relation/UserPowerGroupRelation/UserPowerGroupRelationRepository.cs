using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.UserPowerGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.Relation.UserPowerGroupRelation
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserPowerGroupRelationRepository))]
    public class UserPowerGroupRelationRepository : Repository<SSS.Domain.Permission.Relation.UserPowerGroupRelation.UserPowerGroupRelation>, IUserPowerGroupRelationRepository
    {
        public UserPowerGroupRelationRepository(DbcontextBase context) : base(context)
        {
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联用户
        /// </summary> 
        /// <returns></returns>
        public Pages<List<UserPowerGroupRelationOutputDto>> GetUserByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = @" u.UserName AS username,
	            u.id AS userid,
	            pg.id AS powergroupid,
	            pg.PowerGroupName AS powergroupname,
	            upgr.Id AS id";

            string sql = @"SELECT {0} 
                FROM
	                UserInfo AS u
	                INNER JOIN UserPowerGroupRelation AS upgr ON u.id = upgr.UserId
	                INNER JOIN PowerGroup AS pg ON pg.id = upgr.PowerGroupId
                WHERE
	                u.IsDelete = 0 
	                AND upgr.IsDelete = 0 
	                AND pg.IsDelete =0 ";

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
                var data = Db.Database.SqlQuery<UserPowerGroupRelationOutputDto>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<List<UserPowerGroupRelationOutputDto>>(data.ToList(), count);
            }
            else
            {
                var data = Db.Database.SqlQuery<UserPowerGroupRelationOutputDto>(string.Format(sql, field));
                return new Pages<List<UserPowerGroupRelationOutputDto>>(data?.ToList(), count);
            }
        }

        /// <summary>
        /// 根据用户Id或名称，遍历关联权限组
        /// </summary> 
        /// <returns></returns>
        public Pages<List<UserPowerGroupRelationOutputDto>> GetPowerGroupByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = @" u.UserName AS username,
	            u.id AS userid,
	            pg.id AS powergroupid,
	            pg.PowerGroupName AS powergroupname,
	            upgr.Id AS id";

            string sql = @"SELECT {0} 
                FROM
	                UserInfo AS u
	                INNER JOIN UserPowerGroupRelation AS upgr ON u.id = upgr.UserId
	                INNER JOIN PowerGroup AS pg ON pg.id = upgr.PowerGroupId             
                WHERE
	                u.IsDelete = 0 
	                AND upgr.IsDelete = 0 
	                AND pg.IsDelete =0 ";

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
                var data = Db.Database.SqlQuery<UserPowerGroupRelationOutputDto>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<List<UserPowerGroupRelationOutputDto>>(data.ToList(), count);
            }
            else
            {
                var data = Db.Database.SqlQuery<UserPowerGroupRelationOutputDto>(string.Format(sql, field));
                return new Pages<List<UserPowerGroupRelationOutputDto>>(data?.ToList(), count);
            }
        }
    }
}