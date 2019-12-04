using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Relation.RoleRoleGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Relation.RoleRoleGroupRelation.Service
{
    public interface IRoleRoleGroupRelationService : IQueryService<SSS.Domain.Permission.Relation.RoleRoleGroupRelation.RoleRoleGroupRelation, RoleRoleGroupRelationInputDto, RoleRoleGroupRelationOutputDto>
    {
        void AddRoleRoleGroupRelation(RoleRoleGroupRelationInputDto input);

        Pages<List<RoleRoleGroupRelationOutputDto>> GetListRoleRoleGroupRelation(RoleRoleGroupRelationInputDto input);

        void DeleteRoleRoleGroupRelation(RoleRoleGroupRelationInputDto input);
    }
}