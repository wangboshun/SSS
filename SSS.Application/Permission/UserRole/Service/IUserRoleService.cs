using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.UserRole.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.UserRole.Service
{
    public interface IUserRoleService : IQueryService<SSS.Domain.Permission.UserRole.UserRole, UserRoleInputDto, UserRoleOutputDto>
    {
        void AddUserRole(UserRoleInputDto input);

        Pages<List<UserRoleOutputDto>> GetListUserRole(UserRoleInputDto input);
    }
}