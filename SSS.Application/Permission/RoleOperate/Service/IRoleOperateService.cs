using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.RoleOperate.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.RoleOperate.Service
{
    public interface IRoleOperateService : IQueryService<SSS.Domain.Permission.RoleOperate.RoleOperate, RoleOperateInputDto, RoleOperateOutputDto>
    {
        void AddRoleOperate(RoleOperateInputDto input);

        /// <summary>
        /// 删除角色下的所有操作
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        bool DeleteRoleOperateByRole(string roleid);

        Pages<List<RoleOperateOutputDto>> GetListRoleOperate(RoleOperateInputDto input);

        /// <summary>
        /// 获取角色下的所有操作
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        List<RoleOperateOutputDto> GetOperateByRole(string roleid);
    }
}