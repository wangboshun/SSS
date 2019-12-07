using SSS.Domain.Permission.Relation.PowerGroupOperateRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.PowerGroupOperateRelation
{
    public interface IPowerGroupOperateRelationRepository : IRepository<Domain.Permission.Relation.PowerGroupOperateRelation.PowerGroupOperateRelation>
    {
        /// <summary>
        /// 根据操作Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupOperateRelationOutputDto>> GetPowerGroupByOperate(PowerGroupOperateRelationInputDto input);

        /// <summary>
        /// 根据权限组Id或名称，遍历关联操作
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupOperateRelationOutputDto>> GetOperateByPowerGroup(PowerGroupOperateRelationInputDto input);
    }
}