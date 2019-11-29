using SSS.Domain.Permission.UserRole.Dto;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.UserRole
{
    public interface IUserRoleRepository : IRepository<SSS.Domain.Permission.UserRole.UserRole>
    {
        /// <summary>
        /// 获取角色下所有用户信息
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        List<UserRoleOutputDto> GetUserRoleByRole(string roleid);

        /// <summary>
        /// 根据用户，获取角色信息
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        SSS.Domain.Permission.RoleInfo.RoleInfo GetRoleByUser(string userid);

        /// <summary>
        /// 删除角色下的所有用户
        /// </summary>
        /// <param name="roleid"></param>
        bool DeleteUserRoleByRole(string roleid);
    }
}