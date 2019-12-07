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
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<UserPowerGroupRelationOutputDto>> GetUserByPowerGroup(UserPowerGroupRelationInputDto input)
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

            if (!string.IsNullOrWhiteSpace(input.powergroupid))
                sql += $" AND pg.Id='{input.powergroupid}'";

            if (!string.IsNullOrWhiteSpace(input.powergroupname))
                sql += $" AND pg.PowerGroupName='{input.powergroupname}'";

            if (!string.IsNullOrWhiteSpace(input.parentid))
                sql += $" AND pg.ParentId='{input.parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            var data = Db.Database.SqlQuery<UserPowerGroupRelationOutputDto>(string.Format(sql, field));

            if (data != null && input.pagesize > 0)
                return new Pages<List<UserPowerGroupRelationOutputDto>>(data.Skip(input.pagesize * (input.pageindex > 1 ? input.pageindex - 1 : 0)).Take(input.pagesize).ToList(), count);
            return new Pages<List<UserPowerGroupRelationOutputDto>>(data?.ToList(), count);
        }

        /// <summary>
        /// 根据用户Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<UserPowerGroupRelationOutputDto>> GetPowerGroupByUser(UserPowerGroupRelationInputDto input)
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

            if (!string.IsNullOrWhiteSpace(input.powergroupid))
                sql += $" AND u.Id='{input.powergroupid}'";

            if (!string.IsNullOrWhiteSpace(input.powergroupname))
                sql += $" AND upgr.UserName='{input.powergroupname}'";

            if (!string.IsNullOrWhiteSpace(input.parentid))
                sql += $" AND upgr.ParentId='{input.parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            var data = Db.Database.SqlQuery<UserPowerGroupRelationOutputDto>(string.Format(sql, field));

            if (data != null && input.pagesize > 0)
                return new Pages<List<UserPowerGroupRelationOutputDto>>(data.Skip(input.pagesize * (input.pageindex > 1 ? input.pageindex - 1 : 0)).Take(input.pagesize).ToList(), count);
            return new Pages<List<UserPowerGroupRelationOutputDto>>(data?.ToList(), count);
        }
    }
}