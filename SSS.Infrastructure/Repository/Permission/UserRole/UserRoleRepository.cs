using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.UserRole.Dto;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
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
            var data = from user in Db.UserInfo
                       join user in Db.UserRole
                           on user.Id equals user.UserId into u_ur
                       from u_ur_list in u_ur.DefaultIfEmpty()

                       join role in Db.RoleInfo
                           on u_ur_list.RoleId equals role.Id into r_ur
                       from r_ur_list in r_ur.DefaultIfEmpty()

                       select new UserRoleOutputDto
                       {
                           userid = user.Id,
                           username = user.UserName,
                           createtime = u_ur_list.CreateTime,
                           roleid = r_ur_list.Id,
                           rolename = r_ur_list.RoleName
                       };

            //var result = Db.UserRole.Where(x => x.RoleId.Equals(roleid) && x.IsDelete == 0).Join(Db.UserInfo,
            //    role => role.UserId, user => user.Id,
            //    (role, user) => new UserRoleOutputDto
            //    {
            //        userid = user.Id,
            //        username = user.UserName,
            //        createtime = role.CreateTime
            //    }).ToList();


            return data.ToList();
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
        public bool DeleteUserRoleByRole(string roleid)
        {
            return DeleteList(x => x.RoleId.Equals(roleid), true);
        }
    }
}