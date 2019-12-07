using SSS.Domain.Permission.Relation.UserGroupPowerGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.UserGroupPowerGroupRelation
{
    public interface IUserGroupPowerGroupRelationRepository : IRepository<SSS.Domain.Permission.Relation.UserGroupPowerGroupRelation.UserGroupPowerGroupRelation>
    {
        /// <summary>
        /// ����Ȩ����Id�����ƣ����������û���
        /// </summary> 
        /// <returns></returns>
        Pages<List<UserGroupPowerGroupRelationOutputDto>> GetUserGroupByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// �����û���Id�����ƣ���������Ȩ����
        /// </summary> 
        /// <returns></returns>
        Pages<List<UserGroupPowerGroupRelationOutputDto>> GetPowerGroupByUserGroup(string usergroupid, string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);
    }
}