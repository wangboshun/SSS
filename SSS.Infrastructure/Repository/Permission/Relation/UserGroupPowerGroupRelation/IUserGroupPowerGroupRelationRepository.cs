using SSS.Domain.Permission.Relation.UserGroupPowerGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.UserGroupPowerGroupRelation
{
    public interface IUserGroupPowerGroupRelationRepository : IRepository<SSS.Domain.Permission.Relation.UserGroupPowerGroupRelation.UserGroupPowerGroupRelation>
    {
        /// <summary>
        /// 根据权限组Id或名称，遍历关联用户组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserGroupPowerGroupRelationOutputDto>> GetUserGroupByPowerGroup(UserGroupPowerGroupRelationInputDto input);

        /// <summary>
        /// 根据用户组Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserGroupPowerGroupRelationOutputDto>> GetPowerGroupByUserGroup(UserGroupPowerGroupRelationInputDto input);
    }
}