using Microsoft.Extensions.DependencyInjection;

using MySql.Data.MySqlClient;

using SSS.Domain.Permission.RoleOperate.Dto;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Data.Common;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.RoleOperate
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleOperateRepository))]
    public class RoleOperateRepository : Repository<SSS.Domain.Permission.RoleOperate.RoleOperate>, IRoleOperateRepository
    {
        public RoleOperateRepository(DbcontextBase context) : base(context)
        {
        }

        /// <summary>
        /// 删除角色下的所有操作
        /// </summary>
        /// <param name="roleid"></param>
        public bool DeleteRoleOperateByRole(string roleid)
        {
            return DeleteList(x => x.RoleId.Equals(roleid), true);
        }

        /// <summary>
        /// 获取角色下的所有操作
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        public List<RoleOperateOutputDto> GetRoleOperateByRole(string roleid)
        {
            string sql = @"SELECT o.OperateName AS operatename,
	            o.id AS operateid,
	            r.id AS roleid,
	            r.RoleName AS rolename,
	            ro.id AS id,
	            ro.CreateTime AS createtime 
            FROM
	            OperateInfo AS o
	            INNER JOIN RoleOperate AS ro ON o.id = ro.OperateId
	            INNER JOIN RoleInfo AS r ON r.id = ro.RoleId where ro.RoleId=@roleid";
            return Db.Database.SqlQuery<RoleOperateOutputDto>(sql, new DbParameter[] { new MySqlParameter("roleid", roleid) }).ToList();
        }
    }
}