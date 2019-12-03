using SSS.Application.Seedwork.Service;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;
using SSS.Domain.Permission.Relation.PowerOperateRelation.Dto;

namespace SSS.Application.Permission.Relation.PowerOperateRelation.Service
{
    public interface IPowerOperateRelationService : IQueryService<Domain.Permission.Relation.PowerOperateRelation.PowerOperateRelation,
        PowerOperateRelationInputDto, PowerOperateRelationOutputDto>
    {
        void AddPowerOperateRelation(PowerOperateRelationInputDto input);

        Pages<List<PowerOperateRelationOutputDto>> GetListPowerOperateRelation(PowerOperateRelationInputDto input);

        void DeletePowerOperateRelation(PowerOperateRelationInputDto input);
    }
}