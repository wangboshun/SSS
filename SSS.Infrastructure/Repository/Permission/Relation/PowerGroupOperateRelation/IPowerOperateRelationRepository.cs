using SSS.Domain.Permission.Relation.PowerGroupOperateRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.PowerGroupOperateRelation
{
    public interface IPowerGroupOperateRelationRepository : IRepository<Domain.Permission.Relation.PowerGroupOperateRelation.PowerGroupOperateRelation>
    {
        /// <summary>
        /// ���ݲ���Id�����ƣ���������Ȩ����
        /// </summary> 
        /// <returns></returns>
        Pages<List<PowerGroupOperateRelationOutputDto>> GetPowerGroupByOperate(string operateid, string operatename, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// ����Ȩ����Id�����ƣ�������������
        /// </summary> 
        /// <returns></returns>
        Pages<List<PowerGroupOperateRelationOutputDto>> GetOperateByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);
    }
}