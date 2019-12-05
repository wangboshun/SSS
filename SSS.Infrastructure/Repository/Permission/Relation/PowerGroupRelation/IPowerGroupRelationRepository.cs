using SSS.Domain.Permission.Relation.PowerGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.PowerGroupRelation
{
    public interface IPowerGroupRelationRepository : IRepository<SSS.Domain.Permission.Relation.PowerGroupRelation.PowerGroupRelation>
    {
        /// <summary>
        /// 根据权限Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupRelationOutputDto>> GetPowerGroupByPower(PowerGroupRelationInputDto input);

        /// <summary>
        /// 根据权限组Id或名称，遍历关联权限
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupRelationOutputDto>> GetPowerListByGroup(PowerGroupRelationInputDto input);
    }
}