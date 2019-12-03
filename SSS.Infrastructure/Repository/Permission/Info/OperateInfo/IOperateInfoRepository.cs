using SSS.Domain.Permission.Info.OperateInfo;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Info.OperateInfo
{
    public interface IOperateInfoRepository : IRepository<Domain.Permission.Info.OperateInfo.OperateInfo>
    {
        /// <summary>
        /// 获取操作下的所有下级
        /// </summary>
        /// <param name="operateid"></param>
        /// <returns></returns>
        List<OperateInfoTreeOutputDto> GetChildrenById(string operateid);
    }
}