using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Relation.RoleGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Relation.RoleGroupRelation.Service
{
    public interface IRoleGroupRelationService : IQueryService<SSS.Domain.Permission.Relation.RoleGroupRelation.RoleGroupRelation, RoleGroupRelationInputDto, RoleGroupRelationOutputDto>
    {
        void AddRoleGroupRelation(RoleGroupRelationInputDto input);

        Pages<List<RoleGroupRelationOutputDto>> GetListRoleGroupRelation(RoleGroupRelationInputDto input);

        void DeleteRoleGroupRelation(RoleGroupRelationInputDto input);
    }
}