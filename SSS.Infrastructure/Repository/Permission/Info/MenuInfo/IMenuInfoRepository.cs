using SSS.Domain.Permission.Info.MenuInfo.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Info.MenuInfo
{
    public interface IMenuInfoRepository : IRepository<Domain.Permission.Info.MenuInfo.MenuInfo>
    {
        /// <summary>
        /// ��ȡ�˵��µ������¼�
        /// </summary>
        /// <param name="menuid"></param>
        /// <returns></returns>
        List<MenuInfoTreeOutputDto> GetChildren(string menuid);

        /// <summary>
        ///����Ȩ����Id�����ƣ����������˵�
        /// </summary>
        /// <param name="powergroupid"></param>
        /// <param name="powergroupname"></param>
        /// <param name="parentid"></param>
        /// <param name="pageindex"></param>
        /// <param name="pagesize"></param>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.MenuInfo.MenuInfo>> GetMenuByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);
    }
}