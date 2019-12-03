using SSS.Domain.Permission.Info.OperateInfo;
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
    }
}