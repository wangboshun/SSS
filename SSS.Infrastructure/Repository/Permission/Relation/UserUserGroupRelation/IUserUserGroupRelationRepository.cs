using System.Collections.Generic;
using SSS.Domain.Permission.Relation.UserUserGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

namespace SSS.Infrastructure.Repository.Permission.Relation.UserUserGroupRelation
{
    public interface IUserUserGroupRelationRepository : IRepository<SSS.Domain.Permission.Relation.UserUserGroupRelation.UserUserGroupRelation>
    {
        /// <summary>
        /// 根据用户组Id或名称，遍历关联用户
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserUserGroupRelationOutputDto>> GetUserListByGroup(UserUserGroupRelationInputDto input);

        /// <summary>
        /// 根据用户Id或名称，遍历关联用户组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserUserGroupRelationOutputDto>> GetUserGroupByUser(UserUserGroupRelationInputDto input);
    }
}