using SSS.Application.Seedwork.Service;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;
using SSS.Domain.Permission.Relation.RolePowerRelation.Dto;

namespace SSS.Application.Permission.Relation.RolePowerRelation.Service
{
    public interface IRolePowerRelationService : IQueryService<Domain.Permission.Relation.RolePowerRelation.RolePowerRelation, RolePowerRelationInputDto
        , RolePowerRelationOutputDto>
    {
        void AddRolePowerRelation(RolePowerRelationInputDto input);

        Pages<List<RolePowerRelationOutputDto>> GetListRolePowerRelation(RolePowerRelationInputDto input);

        void DeleteRolePowerRelation(RolePowerRelationInputDto input);
    }
}