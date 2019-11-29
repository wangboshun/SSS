using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.UserRole.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.UserRole.Service
{
    public interface IUserRoleService : IQueryService<SSS.Domain.Permission.UserRole.UserRole, UserRoleInputDto, UserRoleOutputDto>
    {
        void AddUserRole(UserRoleInputDto input);
        void DeleteUserRole(UserRoleInputDto input);
        Pages<List<UserRoleOutputDto>> GetListUserRole(UserRoleInputDto input);

        /// <summary>
        /// 删除角色下的所有用户
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        bool DeleteUserRoleByRole(string roleid);

        /// <summary>
        /// 获取角色下所有用户信息
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        List<UserRoleOutputDto> GetUserByRole(string roleid);
    }
}