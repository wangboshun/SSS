using SSS.Domain.Permission.Info.UserInfo.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Info.UserInfo
{
    public interface IUserInfoRepository : IRepository<Domain.Permission.Info.UserInfo.UserInfo>
    {
        /// <summary>
        /// ��ȡ�û��µ������¼�
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        List<UserInfoTreeOutputDto> GetChildrenById(string userid);

        /// <summary>
        /// �����û���Id�����ƣ����������û�
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.UserInfo.UserInfo>> GetUserByUserGroup(string usergroupid, string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// ����Ȩ����Id�����ƣ����������û�
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.UserInfo.UserInfo>> GetUserByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);
    }
}