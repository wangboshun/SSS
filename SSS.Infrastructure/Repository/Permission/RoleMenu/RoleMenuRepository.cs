using Microsoft.Extensions.DependencyInjection;

using MySql.Data.MySqlClient;

using SSS.Domain.Permission.RoleMenu.Dto;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Data.Common;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.RoleMenu
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleMenuRepository))]
    public class RoleMenuRepository : Repository<SSS.Domain.Permission.RoleMenu.RoleMenu>, IRoleMenuRepository
    {
        public RoleMenuRepository(DbcontextBase context) : base(context)
        {
        }

        /// <summary>
        /// 获取角色下的所有菜单
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        public List<RoleMenuOutputDto> GetRoleMenuByRole(string roleid)
        {
            string sql = @"SELECT m.MenuName AS menuname,
	                    m.id AS menuid,
	                    r.id AS roleid,
	                    r.RoleName AS rolename,
	                    rm.id AS id,
	                    rm.CreateTime AS createtime 
                    FROM
	                    MenuInfo AS m
	                    INNER JOIN RoleMenu AS rm ON m.id = rm.MenuId
	                    INNER JOIN RoleInfo AS r ON r.id = rm.RoleId where rm.RoleId=@roleid";
            return Db.Database.SqlQuery<RoleMenuOutputDto>(sql, new DbParameter[] { new MySqlParameter("roleid", roleid) }).ToList();
        }

        /// <summary>
        /// 删除角色下的所有菜单
        /// </summary>
        /// <param name="roleid"></param>
        public bool DeleteRoleMenuByRole(string roleid)
        {
            return DeleteList(x => x.RoleId.Equals(roleid), true);
        }
    }
}