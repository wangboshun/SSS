using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Group.RoleGroup
{
    public interface IRoleGroupRepository : IRepository<SSS.Domain.Permission.Group.RoleGroup.RoleGroup>
    {
        /// <summary>
        ///  ���ݽ�ɫId�����ƣ�����������ɫ��
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>> GetRoleGroupByRole(string roleid, string rolename, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        ///  ����Ȩ����Id�����ƣ�����������ɫ��
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>> GetRoleGroupByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        ///  �����û���Id�����ƣ�����������ɫ��
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>> GetRoleGroupByUserGroup(string usergroupid, string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        ///  �����û�Id�����ƣ�����������ɫ��
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Group.RoleGroup.RoleGroup>> GetRoleGroupByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0);
    }
}