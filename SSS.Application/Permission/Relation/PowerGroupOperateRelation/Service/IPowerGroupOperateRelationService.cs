using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Relation.PowerGroupOperateRelation.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Relation.PowerGroupOperateRelation.Service
{
    public interface IPowerGroupOperateRelationService : IQueryService<Domain.Permission.Relation.PowerGroupOperateRelation.PowerGroupOperateRelation,
        PowerGroupOperateRelationInputDto, PowerGroupOperateRelationOutputDto>
    {
        void AddPowerGroupOperateRelation(PowerGroupOperateRelationInputDto input);

        Pages<List<PowerGroupOperateRelationOutputDto>> GetListPowerGroupOperateRelation(PowerGroupOperateRelationInputDto input);

        void DeletePowerGroupOperateRelation(PowerGroupOperateRelationInputDto input);
    }
}