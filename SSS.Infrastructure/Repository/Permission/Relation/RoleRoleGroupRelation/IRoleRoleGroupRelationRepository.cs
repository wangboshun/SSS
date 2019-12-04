using SSS.Domain.Permission.Relation.RoleRoleGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.RoleRoleGroupRelation
{
    public interface IRoleRoleGroupRelationRepository : IRepository<SSS.Domain.Permission.Relation.RoleRoleGroupRelation.RoleRoleGroupRelation>
    {
        /// <summary>
        /// 根据角色Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<RoleRoleGroupRelationOutputDto>> GetRoleGroupByRole(RoleRoleGroupRelationInputDto input);

        /// <summary>
        /// 根据角色组Id或名称，遍历关联角色
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<RoleRoleGroupRelationOutputDto>> GetRoleListGroupByGroup(RoleRoleGroupRelationInputDto input);
    }
}