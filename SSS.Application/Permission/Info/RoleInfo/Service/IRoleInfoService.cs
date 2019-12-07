using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Info.RoleInfo.Dto;
using SSS.Domain.Permission.Relation.RoleGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Info.RoleInfo.Service
{
    public interface IRoleInfoService : IQueryService<Domain.Permission.Info.RoleInfo.RoleInfo, RoleInfoInputDto,
        RoleInfoOutputDto>
    {
        void AddRoleInfo(RoleInfoInputDto input);
        Pages<List<RoleInfoOutputDto>> GetListRoleInfo(RoleInfoInputDto input);

        /// <summary>
        ///     ��ȡ��ɫ�µ������¼�
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        List<RoleInfoTreeOutputDto> GetChildren(string roleid);

        void DeleteRoleInfo(RoleInfoInputDto input);

        /// <summary>
        /// ���ݽ�ɫ��Id�����ƣ�����������ɫ
        /// </summary>
        /// <param name="input"></param>
        Pages<List<RoleGroupRelationOutputDto>> GetRoleByRoleGroup(RoleGroupRelationInputDto input);
    }
}