using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.UserUserGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.Relation.UserUserGroupRelation
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserUserGroupRelationRepository))]
    public class UserUserGroupRelationRepository : Repository<SSS.Domain.Permission.Relation.UserUserGroupRelation.UserUserGroupRelation>, IUserUserGroupRelationRepository
    {
        public UserUserGroupRelationRepository(DbcontextBase context) : base(context)
        {
        }

        /// <summary>
        /// 根据用户组Id或名称，遍历关联用户
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<UserUserGroupRelationOutputDto>> GetUserListByGroup(UserUserGroupRelationInputDto input)
        {
            string field = @"u.id AS 'userid',
	                u.UserName AS 'username',
	                ur.UserGroupName AS 'usergroupname',
	                ur.id AS 'usergroupid',
	                uur.id AS 'id'";

            string sql = @"SELECT {0}
                FROM
	                UserInfo AS u
	                INNER JOIN UserUserGroupRelation AS uur ON u.id = uur.UserId
	                INNER JOIN UserGroup AS ur ON uur.UserGroupId = ur.Id 
                WHERE
	                u.IsDelete=0 
	                AND ur.IsDelete=0 
	                AND uur.IsDelete=0 ";

            if (!string.IsNullOrWhiteSpace(input.usergroupid))
                sql += $" AND ur.Id='{input.usergroupid}'";

            if (!string.IsNullOrWhiteSpace(input.usergroupname))
                sql += $" AND ur.UserGroupName='{input.usergroupname}'";

            if (!string.IsNullOrWhiteSpace(input.parentid))
                sql += $" AND ur.ParentId='{input.parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            var data = Db.Database.SqlQuery<UserUserGroupRelationOutputDto>(string.Format(sql, field));

            if (data != null && input.pagesize > 0)
                return new Pages<List<UserUserGroupRelationOutputDto>>(data.Skip(input.pagesize * (input.pageindex > 1 ? input.pageindex - 1 : 0)).Take(input.pagesize).ToList(), count);
            return new Pages<List<UserUserGroupRelationOutputDto>>(data?.ToList(), count);
        }

        /// <summary>
        /// 根据用户Id或名称，遍历关联用户组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<UserUserGroupRelationOutputDto>> GetUserGroupByUser(UserUserGroupRelationInputDto input)
        {
            string field = @"u.id AS 'userid',
	                u.UserName AS 'username',
	                ur.UserGroupName AS 'usergroupname',
	                ur.id AS 'usergroupid',
	                uur.id AS 'id'";

            string sql = @"SELECT {0}
                FROM
	                UserInfo AS u
	                INNER JOIN UserUserGroupRelation AS uur ON u.id=uur.UserId
	                INNER JOIN UserGroup AS ur ON uur.UserGroupId=ur.Id 
                WHERE
	                u.IsDelete=0 
	                AND ur.IsDelete=0 
	                AND uur.IsDelete=0 ";

            if (!string.IsNullOrWhiteSpace(input.userid))
                sql += $" AND u.Id='{input.userid}'";

            if (!string.IsNullOrWhiteSpace(input.username))
                sql += $" AND u.UserName='{input.username}'";

            if (!string.IsNullOrWhiteSpace(input.parentid))
                sql += $" AND u.ParentId='{input.parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            var data = Db.Database.SqlQuery<UserUserGroupRelationOutputDto>(string.Format(sql, field));

            if (data != null && input.pagesize > 0)
                return new Pages<List<UserUserGroupRelationOutputDto>>(data.Skip(input.pagesize * (input.pageindex > 1 ? input.pageindex - 1 : 0)).Take(input.pagesize).ToList(), count);
            return new Pages<List<UserUserGroupRelationOutputDto>>(data?.ToList(), count);
        }
    }
}