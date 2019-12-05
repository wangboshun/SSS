using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.UserGroupPowerGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.Relation.UserGroupPowerGroupRelation
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserGroupPowerGroupRelationRepository))]
    public class UserGroupPowerGroupRelationRepository : Repository<SSS.Domain.Permission.Relation.UserGroupPowerGroupRelation.UserGroupPowerGroupRelation>, IUserGroupPowerGroupRelationRepository
    {
        public UserGroupPowerGroupRelationRepository(DbcontextBase context) : base(context)
        {
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联用户组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<UserGroupPowerGroupRelationOutputDto>> GetUserGroupByPowerGroup(UserGroupPowerGroupRelationInputDto input)
        {
            string field = @" ug.UserGroupName AS usergroupname,
	                ug.id AS usergroupid,
	                pg.id AS powergroupid,
	                pg.PowerGroupName AS powergroupname,
	                ugpgr.Id AS id ";

            string sql = @"SELECT {0} 
                    FROM
	                    UserGroup AS ug
	                    INNER JOIN UserGroupPowerGroupRelation AS ugpgr ON ug.Id = ugpgr.UserGroupId
	                    INNER JOIN PowerGroup AS pg ON pg.Id = ugpgr.PowerGroupId
                    WHERE
	                    ug.IsDelete = 0 
	                    AND pg.IsDelete = 0 
	                    AND ugpgr.IsDelete =0 ";

            if (!string.IsNullOrWhiteSpace(input.powergroupid))
                sql += $" AND pg.Id='{input.powergroupid}'";

            if (!string.IsNullOrWhiteSpace(input.powergroupname))
                sql += $" AND pg.PowerGroupName='{input.powergroupname}'";

            if (!string.IsNullOrWhiteSpace(input.parentid))
                sql += $" AND pg.ParentId='{input.parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            var data = Db.Database.SqlQuery<UserGroupPowerGroupRelationOutputDto>(string.Format(sql, field));

            if (data != null && input.pagesize > 0)
                return new Pages<List<UserGroupPowerGroupRelationOutputDto>>(data.Skip(input.pagesize * (input.pageindex > 1 ? input.pageindex - 1 : 0)).Take(input.pagesize).ToList(), count);
            return new Pages<List<UserGroupPowerGroupRelationOutputDto>>(data?.ToList(), count);
        }

        /// <summary>
        /// 根据用户组Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<UserGroupPowerGroupRelationOutputDto>> GetPowerGroupByUserGroup(UserGroupPowerGroupRelationInputDto input)
        {
            string field = @" ug.UserGroupName AS usergroupname,
	                ug.id AS usergroupid,
	                pg.id AS powergroupid,
	                pg.PowerGroupName AS powergroupname,
	                ugpgr.Id AS id ";

            string sql = @"SELECT {0} 
                    FROM
	                    UserGroup AS ug
	                    INNER JOIN UserGroupPowerGroupRelation AS ugpgr ON ug.Id = ugpgr.UserGroupId
	                    INNER JOIN PowerGroup AS pg ON pg.Id = ugpgr.PowerGroupId
                    WHERE
	                    ug.IsDelete = 0 
	                    AND pg.IsDelete = 0 
	                    AND ugpgr.IsDelete =0 ";

            if (!string.IsNullOrWhiteSpace(input.usergroupid))
                sql += $" AND ug.Id='{input.usergroupid}'";

            if (!string.IsNullOrWhiteSpace(input.usergroupname))
                sql += $" AND ug.UserGroupName='{input.usergroupname}'";

            if (!string.IsNullOrWhiteSpace(input.parentid))
                sql += $" AND ug.ParentId='{input.parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            var data = Db.Database.SqlQuery<UserGroupPowerGroupRelationOutputDto>(string.Format(sql, field));

            if (data != null && input.pagesize > 0)
                return new Pages<List<UserGroupPowerGroupRelationOutputDto>>(data.Skip(input.pagesize * (input.pageindex > 1 ? input.pageindex - 1 : 0)).Take(input.pagesize).ToList(), count);
            return new Pages<List<UserGroupPowerGroupRelationOutputDto>>(data?.ToList(), count);
        }
    }
}