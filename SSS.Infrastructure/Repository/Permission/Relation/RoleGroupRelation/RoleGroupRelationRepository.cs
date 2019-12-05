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
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<RoleGroupRelationOutputDto>> GetRoleGroupByRole(RoleGroupRelationInputDto input)
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

            if (!string.IsNullOrWhiteSpace(input.roleid))
                sql += $" AND r.Id='{input.roleid}'";

            if (!string.IsNullOrWhiteSpace(input.rolename))
                sql += $" AND r.RoleName='{input.rolename}'";

            if (!string.IsNullOrWhiteSpace(input.parentid))
                sql += $" AND r.ParentId='{input.parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            var data = Db.Database.SqlQuery<RoleGroupRelationOutputDto>(string.Format(sql, field));

            if (data != null && input.pagesize > 0)
                return new Pages<List<RoleGroupRelationOutputDto>>(data.Skip(input.pagesize * (input.pageindex > 1 ? input.pageindex - 1 : 0)).Take(input.pagesize).ToList(), count);
            return new Pages<List<RoleGroupRelationOutputDto>>(data?.ToList(), count);
        }

        /// <summary>
        /// 根据角色组Id或名称，遍历关联角色
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<RoleGroupRelationOutputDto>> GetRoleListGroupByGroup(RoleGroupRelationInputDto input)
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

            if (!string.IsNullOrWhiteSpace(input.rolegroupid))
                sql += $" AND rg.Id='{input.rolegroupid}'";

            if (!string.IsNullOrWhiteSpace(input.rolegroupname))
                sql += $" AND rg.RoleGroupName='{input.rolegroupname}'";

            if (!string.IsNullOrWhiteSpace(input.parentid))
                sql += $" AND rg.ParentId='{input.parentid}'";

            int count = Db.Database.Count(string.Format(sql, " count(*) "));

            var data = Db.Database.SqlQuery<RoleGroupRelationOutputDto>(string.Format(sql, field));

            if (data != null && input.pagesize > 0)
                return new Pages<List<RoleGroupRelationOutputDto>>(data.Skip(input.pagesize * (input.pageindex > 1 ? input.pageindex - 1 : 0)).Take(input.pagesize).ToList(), count);
            return new Pages<List<RoleGroupRelationOutputDto>>(data?.ToList(), count);
        }
    }
}