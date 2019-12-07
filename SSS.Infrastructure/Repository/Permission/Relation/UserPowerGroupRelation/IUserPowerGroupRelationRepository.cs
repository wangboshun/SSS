using SSS.Domain.Permission.Relation.UserPowerGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.UserPowerGroupRelation
{
    public interface IUserPowerGroupRelationRepository : IRepository<SSS.Domain.Permission.Relation.UserPowerGroupRelation.UserPowerGroupRelation>
    {
        /// <summary>
        /// ����Ȩ����Id�����ƣ����������û�
        /// </summary> 
        /// <returns></returns>
        Pages<List<UserPowerGroupRelationOutputDto>> GetUserByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// �����û�Id�����ƣ���������Ȩ����
        /// </summary> 
        /// <returns></returns>
        Pages<List<UserPowerGroupRelationOutputDto>> GetPowerGroupByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0);
    }
}