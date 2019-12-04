using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Relation.RoleGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Group.RoleGroup.Service
{
    public interface IRoleGroupService : IQueryService<Domain.Permission.Group.RoleGroup.RoleGroup, RoleGroupInputDto,
        RoleGroupOutputDto>
    {
        void AddRoleGroup(RoleGroupInputDto input);

        Pages<List<RoleGroupOutputDto>> GetListRoleGroup(RoleGroupInputDto input);

        void DeleteRoleGroup(RoleGroupInputDto input);

        /// <summary>
        /// 根据角色Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="input"></param>
        Pages<List<RoleGroupRelationOutputDto>> GetRoleGroupByRole(RoleGroupRelationInputDto input);
    }
}