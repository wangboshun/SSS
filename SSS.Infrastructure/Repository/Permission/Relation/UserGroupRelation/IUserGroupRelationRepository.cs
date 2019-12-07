using SSS.Domain.Permission.Relation.UserGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.UserGroupRelation
{
    public interface IUserGroupRelationRepository : IRepository<SSS.Domain.Permission.Relation.UserGroupRelation.UserGroupRelation>
    {
        /// <summary>
        /// 根据用户组Id或名称，遍历关联用户
        /// </summary> 
        /// <returns></returns>
        Pages<List<UserGroupRelationOutputDto>> GetUserByUserGroup(string usergroupid, string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// 根据用户Id或名称，遍历关联用户组
        /// </summary> 
        /// <returns></returns>
        Pages<List<UserGroupRelationOutputDto>> GetUserGroupByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0);
    }
}