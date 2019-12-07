using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.RoleGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.Relation.RoleGroupRelation
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleGroupRelationRepository))]
    public class RoleGroupRelationRepository : Repository<SSS.Domain.Permission.Relation.RoleGroupRelation.RoleGroupRelation>, IRoleGroupRelationRepository
    {
        public RoleGroupRelationRepository(DbcontextBase context) : base(context)
        {
        }

        /// <summary>
        /// 根据角色Id或名称，遍历关联角色组
        /// </summary> 
        /// <returns></returns>
        public Pages<List<RoleGroupRelationOutputDto>> GetRoleGroupByRole(string roleid, string rolename, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = @"r.id AS 'roleid',
	            r.RoleName AS 'rolename',
	            rg.RoleGroupName AS 'rolegroupname',
	            rg.id AS 'rolegroupid',
                rrr.CreateTime as createtime,
	            rrr.id AS 'id' ";

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
                var data = Db.Database.SqlQuery<RoleGroupRelationOutputDto>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<List<RoleGroupRelationOutputDto>>(data.ToList(), count);
            }
            else
            {
                var data = Db.Database.SqlQuery<RoleGroupRelationOutputDto>(string.Format(sql, field));
                return new Pages<List<RoleGroupRelationOutputDto>>(data?.ToList(), count);
            }
        }

        /// <summary>
        /// 根据角色组Id或名称，遍历关联角色
        /// </summary> 
        /// <returns></returns>
        public Pages<List<RoleGroupRelationOutputDto>> GetRoleByRoleGroup(string rolegroupid, string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            string field = @"r.id AS 'roleid',
	            r.RoleName AS 'rolename',
	            rg.RoleGroupName AS 'rolegroupname',
	            rg.id AS 'rolegroupid',
                rrr.CreateTime as createtime,
	            rrr.id AS 'id' ";

            string sql = @"SELECT {0}
                                FROM
	                RoleInfo AS r
	                INNER JOIN RoleGroupRelation AS rrr ON r.id = rrr.RoleId
	                INNER JOIN RoleGroup AS rg ON rrr.RoleGroupId = rg.Id 
                WHERE
	   	            r.IsDelete=0 
	                AND rg.IsDelete=0 
	                AND rrr.IsDelete=0 ";

            if (!string.IsNullOrWhiteSpace(rolegroupid))
                sql += $" AND rg.Id='{rolegroupid}'";

            if (!string.IsNullOrWhiteSpace(rolegroupname))
                sql += $" AND rg.RoleGroupName='{rolegroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND rg.ParentId='{parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            if (pageindex > 0 && pagesize > 0)
            {
                string limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<RoleGroupRelationOutputDto>(string.Format(sql + limit, field, pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<List<RoleGroupRelationOutputDto>>(data.ToList(), count);
            }
            else
            {
                var data = Db.Database.SqlQuery<RoleGroupRelationOutputDto>(string.Format(sql, field));
                return new Pages<List<RoleGroupRelationOutputDto>>(data?.ToList(), count);
            }
        }
    }
}