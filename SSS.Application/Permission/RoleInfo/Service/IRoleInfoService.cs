using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.RoleInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.RoleInfo.Service
{
    public interface IRoleInfoService : IQueryService<SSS.Domain.Permission.RoleInfo.RoleInfo, RoleInfoInputDto, RoleInfoOutputDto>
    {
        void AddRoleInfo(RoleInfoInputDto input);
        Pages<List<RoleInfoOutputDto>> GetListRoleInfo(RoleInfoInputDto input);
        List<RoleInfoTreeOutputDto> GetChildren(RoleInfoInputDto input);
    }
}