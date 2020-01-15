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
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.MenuInfo.MenuInfo>> GetMenuByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// ���ݽ�ɫ��Id�����ƣ����������˵�
        /// </summary>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.MenuInfo.MenuInfo>> GetMenuByRoleGroup(string rolegroupid, string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        ///�����û�Id�����ƣ����������˵�
        /// </summary>
        /// <param name="userid"></param>
        /// <param name="username"></param>
        /// <param name="parentid"></param>
        /// <param name="pageindex"></param>
        /// <param name="pagesize"></param>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.MenuInfo.MenuInfo>> GetMenuByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        ///�����û���Id�����ƣ����������˵�
        /// </summary>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.MenuInfo.MenuInfo>> GetMenuByUserGroup(string usergroupid, string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);
    }
}