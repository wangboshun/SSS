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
        ///     获取角色下的所有下级
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        List<RoleInfoTreeOutputDto> GetChildren(string roleid);

        void DeleteRoleInfo(RoleInfoInputDto input);

        /// <summary>
        /// 根据角色组Id或名称，遍历关联角色
        /// </summary>
        /// <param name="input"></param>
        Pages<List<RoleGroupRelationOutputDto>> GetRoleByRoleGroup(RoleGroupRelationInputDto input);
    }
}