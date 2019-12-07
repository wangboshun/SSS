using SSS.Domain.Permission.Relation.PowerGroupMenuRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.PowerGroupMenuRelation
{
    public interface IPowerGroupMenuRelationRepository : IRepository<Domain.Permission.Relation.PowerGroupMenuRelation.PowerGroupMenuRelation>
    {
        /// <summary>
        /// ���ݲ˵�Id�����ƣ���������Ȩ����
        /// </summary> 
        /// <returns></returns>
        Pages<List<PowerGroupMenuRelationOutputDto>> GetPowerGroupByMenu(string menuid, string menuname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// ����Ȩ����Id�����ƣ����������˵�
        /// </summary> 
        /// <returns></returns>
        Pages<List<PowerGroupMenuRelationOutputDto>> GetMenuByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);
    }
}