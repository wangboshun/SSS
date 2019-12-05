using SSS.Domain.Permission.Relation.PowerGroupMenuRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.PowerGroupMenuRelation
{
    public interface IPowerGroupMenuRelationRepository : IRepository<Domain.Permission.Relation.PowerGroupMenuRelation.PowerGroupMenuRelation>
    {
        /// <summary>
        /// ���ݲ˵�Id�����ƣ���������Ȩ����
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupMenuRelationOutputDto>> GetPowerGroupByMenu(PowerGroupMenuRelationInputDto input);

        /// <summary>
        /// ����Ȩ����Id�����ƣ����������˵�
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupMenuRelationOutputDto>> GetMenuListByPowerGroup(PowerGroupMenuRelationInputDto input);
    }
}