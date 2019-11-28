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
        /// 根据RoleId获取映射信息
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        public List<UserRoleOutputDto> GetUserByRole(string roleid)
        {
            var result = Db.UserRole.Where(x => x.RoleId.Equals(roleid) && x.IsDelete == 0).Join(Db.UserInfo,
                role => role.UserId, user => user.Id,
                (role, user) => new UserRoleOutputDto
                {
                    id = role.RoleId,
                    userid = user.Id,
                    username = user.UserName,
                    createtime = role.CreateTime
                }).ToList();
            return result;
        }

        /// <summary>
        /// 根据UserId获取角色信息
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        public SSS.Domain.Permission.RoleInfo.RoleInfo GetRoleByUser(string userid)
        {
            var data = Db.Database.SqlQuery<SSS.Domain.Permission.RoleInfo.RoleInfo>($"SELECT role.* FROM UserRole AS ur INNER JOIN RoleInfo AS role ON ur.RoleId = role.Id WHERE ur.UserId='{userid}'  and ur.IsDelete=0 ");
            return data?.FirstOrDefault();
        }
    }
}