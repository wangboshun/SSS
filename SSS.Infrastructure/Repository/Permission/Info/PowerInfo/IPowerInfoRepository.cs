using SSS.Domain.Permission.Info.PowerInfo.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Info.PowerInfo
{
    public interface IPowerInfoRepository : IRepository<Domain.Permission.Info.PowerInfo.PowerInfo>
    {
        /// <summary>
        /// ��ȡȨ���µ������¼�
        /// </summary>
        /// <param name="powerid"></param>
        /// <returns></returns>
        List<PowerInfoTreeOutputDto> GetChildren(string powerid);

        /// <summary>
        /// �����û�Id�����ƣ���������Ȩ��
        /// </summary>
        /// <param name="userid"></param>
        /// <param name="username"></param>
        /// <param name="parentid"></param>
        /// <param name="pageindex"></param>
        /// <param name="pagesize"></param>
        /// <returns></returns>
        Pages<List<Domain.Permission.Info.PowerInfo.PowerInfo>> GetPowerByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// ����Ȩ����Id�����ƣ���������Ȩ��
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.PowerInfo.PowerInfo>> GetPowerByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);
    }
}