using SSS.Domain.Permission.Relation.UserGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.UserGroupRelation
{
    public interface IUserGroupRelationRepository : IRepository<SSS.Domain.Permission.Relation.UserGroupRelation.UserGroupRelation>
    {
        /// <summary>
        /// �����û���Id�����ƣ����������û�
        /// </summary> 
        /// <returns></returns>
        Pages<List<UserGroupRelationOutputDto>> GetUserByUserGroup(string usergroupid, string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// �����û�Id�����ƣ����������û���
        /// </summary> 
        /// <returns></returns>
        Pages<List<UserGroupRelationOutputDto>> GetUserGroupByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0);
    }
}