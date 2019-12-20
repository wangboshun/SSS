using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Group.PowerGroup
{
    public interface IPowerGroupRepository : IRepository<SSS.Domain.Permission.Group.PowerGroup.PowerGroup>
    {

        /// <summary>
        ///  ����Ȩ��Id�����ƣ���������Ȩ����
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Group.PowerGroup.PowerGroup>> GetPowerGroupByPower(string powerid, string powername, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// ���ݲ˵�Id�����ƣ���������Ȩ����
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Group.PowerGroup.PowerGroup>> GetPowerGroupByMenu(string menuid, string menuname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        ///  ���ݲ���Id�����ƣ���������Ȩ����
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Group.PowerGroup.PowerGroup>> GetPowerGroupByOperate(string operateid, string operatename, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        ///  �����û�Id�����ƣ���������Ȩ����
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Group.PowerGroup.PowerGroup>> GetPowerGroupByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        ///  �����û���Id�����ƣ���������Ȩ����
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Group.PowerGroup.PowerGroup>> GetPowerGroupByUserGroup(string usergroupid, string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        ///  ���ݽ�ɫ��Id�����ƣ���������Ȩ����
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Group.PowerGroup.PowerGroup>> GetPowerGroupByRoleGroup(string rolegroupid, string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

    }
}