using SSS.Domain.Permission.UserRole.Dto;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.UserRole
{
    public interface IUserRoleRepository : IRepository<SSS.Domain.Permission.UserRole.UserRole>
    {
        List<UserRoleOutputDto> GetUserByRole(string roleid);
        SSS.Domain.Permission.RoleInfo.RoleInfo GetRoleByUser(string userid);
    }
}