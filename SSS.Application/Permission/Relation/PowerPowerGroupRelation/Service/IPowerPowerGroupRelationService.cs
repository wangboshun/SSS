using SSS.Domain.Permission.Relation.PowerPowerGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using System.Collections.Generic;
using SSS.Application.Seedwork.Service;

namespace SSS.Application.Permission.Relation.PowerPowerGroupRelation.Service
{
    public interface IPowerPowerGroupRelationService : IQueryService<SSS.Domain.Permission.Relation.PowerPowerGroupRelation.PowerPowerGroupRelation, PowerPowerGroupRelationInputDto, PowerPowerGroupRelationOutputDto>
    {
        void AddPowerPowerGroupRelation(PowerPowerGroupRelationInputDto input);

		Pages<List<PowerPowerGroupRelationOutputDto>> GetListPowerPowerGroupRelation(PowerPowerGroupRelationInputDto input);

        void DeletePowerPowerGroupRelation(PowerPowerGroupRelationInputDto input);
    }
}