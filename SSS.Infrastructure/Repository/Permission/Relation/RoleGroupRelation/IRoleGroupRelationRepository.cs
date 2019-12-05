using SSS.Domain.Permission.Relation.RoleGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.RoleGroupRelation
{
    public interface IRoleGroupRelationRepository : IRepository<SSS.Domain.Permission.Relation.RoleGroupRelation.RoleGroupRelation>
    {
        /// <summary>
        /// 根据角色Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<RoleGroupRelationOutputDto>> GetRoleGroupByRole(RoleGroupRelationInputDto input);

        /// <summary>
        /// 根据角色组Id或名称，遍历关联角色
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<RoleGroupRelationOutputDto>> GetRoleListGroupByGroup(RoleGroupRelationInputDto input);
    }
}