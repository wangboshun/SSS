using SSS.Domain.Permission.Info.RoleInfo.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Info.RoleInfo
{
    public interface IRoleInfoRepository : IRepository<Domain.Permission.Info.RoleInfo.RoleInfo>
    {
        /// <summary>
        /// ��ȡ��ɫ�µ������¼�
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        List<RoleInfoTreeOutputDto> GetChildren(string roleid);

        /// <summary>
        /// ����Ȩ����Id�����ƣ�����������ɫ
        /// </summary>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.RoleInfo.RoleInfo>> GetRoleByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// ���ݽ�ɫ��Id�����ƣ�����������ɫ
        /// </summary>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.RoleInfo.RoleInfo>> GetRoleByRoleGroup(string rolegroupid, string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// �����û�Id�����ƣ�����������ɫ
        /// </summary>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.RoleInfo.RoleInfo>> GetRoleByUser(string userid, string usename, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// �����û���Id�����ƣ�����������ɫ
        /// </summary>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.RoleInfo.RoleInfo>> GetRoleByUserGroup(string usergroupid, string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);
    }
}