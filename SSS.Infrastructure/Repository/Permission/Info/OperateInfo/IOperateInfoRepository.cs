using SSS.Domain.Permission.Info.OperateInfo;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Info.OperateInfo
{
    public interface IOperateInfoRepository : IRepository<Domain.Permission.Info.OperateInfo.OperateInfo>
    {
        /// <summary>
        /// ��ȡ�����µ������¼�
        /// </summary>
        /// <param name="operateid"></param>
        /// <returns></returns>
        List<OperateInfoTreeOutputDto> GetChildrenById(string operateid);

        /// <summary>
        ///����Ȩ����Id�����ƣ�������������
        /// </summary> 
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.OperateInfo.OperateInfo>> GetOperateByPowerGroup(string powergroupid, string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// �����û�Id�����ƣ�������������
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.OperateInfo.OperateInfo>> GetOperateByUser(string userid, string username, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// �����û���Id�����ƣ�������������
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.OperateInfo.OperateInfo>> GetOperateByUserGroup(string usergroupid, string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// ���ݽ�ɫ��Id�����ƣ�������������
        /// </summary> 
        /// <returns></returns>
        Pages<IEnumerable<Domain.Permission.Info.OperateInfo.OperateInfo>> GetOperateByRoleGroup(string rolegroupid, string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0);
    }
}