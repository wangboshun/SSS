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
        /// <returns></returns>
        public Pages<List<UserGroupPowerGroupRelationOutputDto>> GetUserGroupByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
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
                var data = Db.Database.SqlQuery<UserGroupPowerGroupRelationOutputDto>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<List<UserGroupPowerGroupRelationOutputDto>>(data.ToList(), count);
            }
            else
            {
                var data = Db.Database.SqlQuery<UserGroupPowerGroupRelationOutputDto>(string.Format(sql, field));
                return new Pages<List<UserGroupPowerGroupRelationOutputDto>>(data?.ToList(), count);
            }
        }

        /// <summary>
        /// 根据用户组Id或名称，遍历关联权限组
        /// </summary> 
        /// <returns></returns>
        public Pages<List<UserGroupPowerGroupRelationOutputDto>> GetPowerGroupByUserGroup(string usergroupid, string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
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

            if (!string.IsNullOrWhiteSpace(usergroupid))
                sql += $" AND ug.Id='{usergroupid}'";

            if (!string.IsNullOrWhiteSpace(usergroupname))
                sql += $" AND ug.UserGroupName='{usergroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND ug.ParentId='{parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            if (pageindex > 0 && pagesize > 0)
            {
                string limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<UserGroupPowerGroupRelationOutputDto>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<List<UserGroupPowerGroupRelationOutputDto>>(data.ToList(), count);
            }
            else
            {
                var data = Db.Database.SqlQuery<UserGroupPowerGroupRelationOutputDto>(string.Format(sql, field));
                return new Pages<List<UserGroupPowerGroupRelationOutputDto>>(data?.ToList(), count);
            }
        }
    }
}