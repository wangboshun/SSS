using SSS.Domain.Permission.Relation.PowerGroupOperateRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.PowerGroupOperateRelation
{
    public interface IPowerGroupOperateRelationRepository : IRepository<Domain.Permission.Relation.PowerGroupOperateRelation.PowerGroupOperateRelation>
    {
        /// <summary>
        /// ���ݲ���Id�����ƣ���������Ȩ����
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupOperateRelationOutputDto>> GetPowerGroupByOperate(PowerGroupOperateRelationInputDto input);

        /// <summary>
        /// ����Ȩ����Id�����ƣ�������������
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupOperateRelationOutputDto>> GetOperateByPowerGroup(PowerGroupOperateRelationInputDto input);
    }
}