using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Relation.PowerMenuRelation.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Relation.PowerMenuRelation.Service
{
    public interface IPowerMenuRelationService : IQueryService<Domain.Permission.Relation.PowerMenuRelation.PowerMenuRelation, PowerMenuRelationInputDto
        , PowerMenuRelationOutputDto>
    {
        void AddPowerMenuRelation(PowerMenuRelationInputDto input);

        Pages<List<PowerMenuRelationOutputDto>> GetListPowerMenuRelation(PowerMenuRelationInputDto input);

        void DeletePowerMenuRelation(PowerMenuRelationInputDto input);
    }
}