using SSS.Domain.Permission.Relation.PowerPowerGroupRelation.Dto;
using SSS.Domain.Permission.Relation.UserUserGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.PowerPowerGroupRelation
{
    public interface IPowerPowerGroupRelationRepository : IRepository<SSS.Domain.Permission.Relation.PowerPowerGroupRelation.PowerPowerGroupRelation>
    {
        /// <summary>
        /// 根据权限Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerPowerGroupRelationOutputDto>> GetPowerGroupByPower(PowerPowerGroupRelationInputDto input);

        /// <summary>
        /// 根据权限组Id或名称，遍历关联权限
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerPowerGroupRelationOutputDto>> GetPowerListByGroup(PowerPowerGroupRelationInputDto input);
    }
}