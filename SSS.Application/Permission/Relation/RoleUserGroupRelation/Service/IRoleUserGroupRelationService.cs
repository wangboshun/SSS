using SSS.Domain.Permission.Relation.RoleUserGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using System.Collections.Generic;
using SSS.Application.Seedwork.Service;

namespace SSS.Application.Permission.Relation.RoleUserGroupRelation.Service
{
    public interface IRoleUserGroupRelationService : IQueryService<SSS.Domain.Permission.Relation.RoleUserGroupRelation.RoleUserGroupRelation, RoleUserGroupRelationInputDto, RoleUserGroupRelationOutputDto>
    {
        void AddRoleUserGroupRelation(RoleUserGroupRelationInputDto input);

		Pages<List<RoleUserGroupRelationOutputDto>> GetListRoleUserGroupRelation(RoleUserGroupRelationInputDto input);

        void DeleteRoleUserGroupRelation(RoleUserGroupRelationInputDto input); 
    }
}