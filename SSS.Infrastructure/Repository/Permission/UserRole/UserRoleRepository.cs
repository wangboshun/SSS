using Microsoft.Extensions.DependencyInjection;

using MySql.Data.MySqlClient;

using SSS.Domain.Permission.UserRole.Dto;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Data.Common;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.UserRole
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserRoleRepository))]
    public class UserRoleRepository : Repository<SSS.Domain.Permission.UserRole.UserRole>, IUserRoleRepository
    {
        public UserRoleRepository(DbcontextBase context) : base(context)
        {
        }

        /// <summary>
        /// 获取角色下所有用户信息
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        public List<UserRoleOutputDto> GetUserRoleByRole(string roleid)
        {
            string sql = @"SELECT u.UserName AS username,
	                        u.id AS userid,
	                        r.id AS roleid,
	                        r.RoleName AS rolename,
	                        ur.id AS id,
	                        ur.CreateTime AS createtime 
                        FROM
	                        UserInfo AS u
	                        INNER JOIN UserRole AS ur ON u.id = ur.UserId
	                        INNER JOIN RoleInfo AS r ON r.id = ur.RoleId where  ur.IsDelete=0 and r.IsDelete=0 and u.IsDelete=0 and ur.RoleId=@roleid";
            return Db.Database.SqlQuery<UserRoleOutputDto>(sql, new DbParameter[] { new MySqlParameter("roleid", roleid) })?.ToList();
        }

        /// <summary>
        /// 根据用户，获取角色信息
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        public SSS.Domain.Permission.RoleInfo.RoleInfo GetRoleByUser(string userid)
        {
            var data = Db.Database.SqlQuery<SSS.Domain.Permission.RoleInfo.RoleInfo>($"SELECT role.* FROM UserRole AS ur INNER JOIN RoleInfo AS role ON ur.RoleId = role.Id WHERE ur.UserId='{userid}'  and ur.IsDelete=0 ");
            return data?.FirstOrDefault();
        }

        /// <summary>
        /// 删除角色下的所有用户
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        public bool DeleteUserRoleByRole(string roleid, bool save = true)
        {
            return DeleteList(x => x.RoleId.Equals(roleid), save);
        }
    }
}