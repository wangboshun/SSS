using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Group.RoleGroup
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleGroupRepository))]
    public class RoleGroupRepository : Repository<SSS.Domain.Permission.Group.RoleGroup.RoleGroup>, IRoleGroupRepository
    {
        public RoleGroupRepository(DbcontextBase context) : base(context)
        {

        }

        /// <summary>
        /// 根据角色Id或名称，遍历关联角色组
        /// </summary> 
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>> GetRoleGroupByRole(string roleid, string rolename, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = " rg.* ";

            string sql = @"SELECT {0}
                                FROM
	                RoleInfo AS r
	                INNER JOIN RoleGroupRelation AS rrr ON r.id = rrr.RoleId
	                INNER JOIN RoleGroup AS rg ON rrr.RoleGroupId = rg.Id 
                WHERE
	   	            r.IsDelete=0 
	                AND rg.IsDelete=0 
	                AND rrr.IsDelete=0";

            if (!string.IsNullOrWhiteSpace(roleid))
                sql += $" AND r.Id='{roleid}'";

            if (!string.IsNullOrWhiteSpace(rolename))
                sql += $" AND r.RoleName='{rolename}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND r.ParentId='{parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            if (pageindex > 0 && pagesize > 0)
            {
                string limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<Domain.Permission.Group.RoleGroup.RoleGroup>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>>(data, count);
            }
            else
            {
                var data = Db.Database.SqlQuery<Domain.Permission.Group.RoleGroup.RoleGroup>(string.Format(sql, field));
                return new Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>>(data, count);
            }
        }
    }
}