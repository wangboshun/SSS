using SSS.Domain.Permission.Relation.PowerGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.PowerGroupRelation
{
    public interface IPowerGroupRelationRepository : IRepository<SSS.Domain.Permission.Relation.PowerGroupRelation.PowerGroupRelation>
    {
        /// <summary>
        /// ����Ȩ��Id�����ƣ���������Ȩ����
        /// </summary> 
        /// <returns></returns>
        Pages<List<PowerGroupRelationOutputDto>> GetPowerGroupByPower(string powerid, string powername, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// ����Ȩ����Id�����ƣ���������Ȩ��
        /// </summary> 
        /// <returns></returns>
        Pages<List<PowerGroupRelationOutputDto>> GetPowerByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);
    }
}