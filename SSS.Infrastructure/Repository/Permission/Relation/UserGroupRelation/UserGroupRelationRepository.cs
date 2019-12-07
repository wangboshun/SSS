using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.UserGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.Relation.UserGroupRelation
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserGroupRelationRepository))]
    public class UserGroupRelationRepository : Repository<SSS.Domain.Permission.Relation.UserGroupRelation.UserGroupRelation>, IUserGroupRelationRepository
    {
        public UserGroupRelationRepository(DbcontextBase context) : base(context)
        {
        }

        /// <summary>
        /// 根据用户组Id或名称，遍历关联用户
        /// </summary> 
        /// <returns></returns>
        public Pages<List<UserGroupRelationOutputDto>> GetUserByUserGroup(string usergroupid, string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = @"u.id AS 'userid',
	                u.UserName AS 'username',
	                ur.UserGroupName AS 'usergroupname',
	                ur.id AS 'usergroupid',
                    uur.CreateTime as createtime,
	                uur.id AS 'id'";

            string sql = @"SELECT {0}
                FROM
	                UserInfo AS u
	                INNER JOIN UserGroupRelation AS uur ON u.id = uur.UserId
	                INNER JOIN UserGroup AS ur ON uur.UserGroupId = ur.Id 
                WHERE
	                u.IsDelete=0 
	                AND ur.IsDelete=0 
	                AND uur.IsDelete=0 ";

            if (!string.IsNullOrWhiteSpace(usergroupid))
                sql += $" AND ur.Id='{usergroupid}'";

            if (!string.IsNullOrWhiteSpace(usergroupname))
                sql += $" AND ur.UserGroupName='{usergroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND ur.ParentId='{parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            if (pageindex > 0 && pagesize > 0)
            {
                string limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<UserGroupRelationOutputDto>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<List<UserGroupRelationOutputDto>>(data.ToList(), count);
            }
            else
            {
                var data = Db.Database.SqlQuery<UserGroupRelationOutputDto>(string.Format(sql, field));
                return new Pages<List<UserGroupRelationOutputDto>>(data?.ToList(), count);
            }
        }

        /// <summary>
        /// 根据用户Id或名称，遍历关联用户组
        /// </summary> 
        /// <returns></returns>
        public Pages<List<UserGroupRelationOutputDto>> GetUserGroupByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = @"u.id AS 'userid',
	                u.UserName AS 'username',
	                ur.UserGroupName AS 'usergroupname',
	                ur.id AS 'usergroupid',
                    uur.CreateTime as createtime,
	                uur.id AS 'id'";

            string sql = @"SELECT {0}
                FROM
	                UserInfo AS u
	                INNER JOIN UserGroupRelation AS uur ON u.id=uur.UserId
	                INNER JOIN UserGroup AS ur ON uur.UserGroupId=ur.Id 
                WHERE
	                u.IsDelete=0 
	                AND ur.IsDelete=0 
	                AND uur.IsDelete=0 ";

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
                var data = Db.Database.SqlQuery<UserGroupRelationOutputDto>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<List<UserGroupRelationOutputDto>>(data.ToList(), count);
            }
            else
            {
                var data = Db.Database.SqlQuery<UserGroupRelationOutputDto>(string.Format(sql, field));
                return new Pages<List<UserGroupRelationOutputDto>>(data?.ToList(), count);
            }
        }
    }
}