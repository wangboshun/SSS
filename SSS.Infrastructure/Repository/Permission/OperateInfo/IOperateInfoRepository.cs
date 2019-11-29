using SSS.Domain.Permission.OperateInfo;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.OperateInfo
{
    public interface IOperateInfoRepository : IRepository<SSS.Domain.Permission.OperateInfo.OperateInfo>
    {
        /// <summary>
        /// ��ȡ�����µ������¼�
        /// </summary>
        /// <param name="operateid"></param>
        /// <returns></returns>
        List<OperateInfoTreeOutputDto> GetChildrenById(string operateid);
    }
}