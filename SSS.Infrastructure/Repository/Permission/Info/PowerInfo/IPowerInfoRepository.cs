using SSS.Domain.Permission.Info.PowerInfo.Dto;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Info.PowerInfo
{
    public interface IPowerInfoRepository : IRepository<Domain.Permission.Info.PowerInfo.PowerInfo>
    {
        /// <summary>
        /// 获取权限下的所有下级
        /// </summary>
        /// <param name="powerid"></param>
        /// <returns></returns>
        List<PowerInfoTreeOutputDto> GetChildren(string powerid);
    }
}