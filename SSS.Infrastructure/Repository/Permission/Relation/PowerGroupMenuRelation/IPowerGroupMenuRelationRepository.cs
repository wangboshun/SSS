using SSS.Domain.Permission.Relation.PowerGroupMenuRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.PowerGroupMenuRelation
{
    public interface IPowerGroupMenuRelationRepository : IRepository<Domain.Permission.Relation.PowerGroupMenuRelation.PowerGroupMenuRelation>
    {
        /// <summary>
        /// 根据菜单Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupMenuRelationOutputDto>> GetPowerGroupByMenu(PowerGroupMenuRelationInputDto input);

        /// <summary>
        /// 根据权限组Id或名称，遍历关联菜单
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupMenuRelationOutputDto>> GetMenuListByPowerGroup(PowerGroupMenuRelationInputDto input);
    }
}