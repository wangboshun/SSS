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
        /// ɾ����ɫ�µ����в���
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        bool DeleteRoleOperateByRole(string roleid);

        Pages<List<RoleOperateOutputDto>> GetListRoleOperate(RoleOperateInputDto input);

        /// <summary>
        /// ��ȡ��ɫ�µ����в���
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        List<RoleOperateOutputDto> GetOperateByRole(string roleid);
    }
}