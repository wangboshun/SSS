using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Relation.PowerGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Relation.PowerGroupRelation.Service
{
    public interface IPowerGroupRelationService : IQueryService<SSS.Domain.Permission.Relation.PowerGroupRelation.PowerGroupRelation, PowerGroupRelationInputDto, PowerGroupRelationOutputDto>
    {
        void AddPowerGroupRelation(PowerGroupRelationInputDto input);

        Pages<List<PowerGroupRelationOutputDto>> GetListPowerGroupRelation(PowerGroupRelationInputDto input);

        void DeletePowerGroupRelation(PowerGroupRelationInputDto input);
    }
}