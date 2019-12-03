using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;
using SSS.Domain.Permission.Relation.PowerPowerGroupRelation.Dto;

namespace SSS.Application.Permission.Group.PowerGroup.Service
{
    public interface IPowerGroupService : IQueryService<Domain.Permission.Group.PowerGroup.PowerGroup,
        PowerGroupInputDto, PowerGroupOutputDto>
    {
        void AddPowerGroup(PowerGroupInputDto input);

        Pages<List<PowerGroupOutputDto>> GetListPowerGroup(PowerGroupInputDto input);

        void DeletePowerGroup(PowerGroupInputDto input);

        /// <summary>
        /// ����Ȩ��Id�����ƣ���������Ȩ����
        /// </summary>
        /// <param name="input"></param>
        Pages<List<PowerPowerGroupRelationOutputDto>>  GetPowerGroupByPower(PowerPowerGroupRelationInputDto input);
    }
}