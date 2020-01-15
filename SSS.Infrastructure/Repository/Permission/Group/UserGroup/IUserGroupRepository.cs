using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Group.UserGroup
{
    public interface IUserGroupRepository : IRepository<SSS.Domain.Permission.Group.UserGroup.UserGroup>
    {
        /// <summary>
        /// ����Ȩ����Id�����ƣ����������û���
        /// </summary>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Group.UserGroup.UserGroup>> GetUserGroupByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// ���ݽ�ɫ��Id�����ƣ����������û���
        /// </summary>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Group.UserGroup.UserGroup>> GetUserGroupByRoleGroup(string rolegroupid, string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// �����û�Id�����ƣ����������û���
        /// </summary>
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Group.UserGroup.UserGroup>> GetUserGroupByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0);
    }
}