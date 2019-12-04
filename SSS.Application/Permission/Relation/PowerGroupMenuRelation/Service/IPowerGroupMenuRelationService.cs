using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Relation.PowerGroupMenuRelation.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Relation.PowerGroupMenuRelation.Service
{
    public interface IPowerGroupMenuRelationService : IQueryService<Domain.Permission.Relation.PowerGroupMenuRelation.PowerGroupMenuRelation, PowerGroupMenuRelationInputDto
        , PowerGroupMenuRelationOutputDto>
    {
        void AddPowerGroupMenuRelation(PowerGroupMenuRelationInputDto input);

        Pages<List<PowerGroupMenuRelationOutputDto>> GetListPowerGroupMenuRelation(PowerGroupMenuRelationInputDto input);

        void DeletePowerGroupMenuRelation(PowerGroupMenuRelationInputDto input);
    }
}