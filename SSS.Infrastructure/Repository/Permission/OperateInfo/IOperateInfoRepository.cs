using SSS.Domain.Permission.OperateInfo;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.OperateInfo
{
    public interface IOperateInfoRepository : IRepository<SSS.Domain.Permission.OperateInfo.OperateInfo>
    {
        /// <summary>
        /// 获取操作下的所有下级
        /// </summary>
        /// <param name="operateid"></param>
        /// <returns></returns>
        List<OperateInfoTreeOutputDto> GetChildrenById(string operateid);
    }
}