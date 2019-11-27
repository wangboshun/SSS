using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.RoleOperate.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.RoleOperate.Service
{
    public interface IRoleOperateService : IQueryService<SSS.Domain.Permission.RoleOperate.RoleOperate, RoleOperateInputDto, RoleOperateOutputDto>
    {
        void AddRoleOperate(RoleOperateInputDto input);

        Pages<List<RoleOperateOutputDto>> GetListRoleOperate(RoleOperateInputDto input);
    }
}