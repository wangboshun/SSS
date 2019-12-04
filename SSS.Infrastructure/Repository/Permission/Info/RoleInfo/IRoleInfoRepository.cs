using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;
using SSS.Domain.Permission.Info.RoleInfo.Dto;

namespace SSS.Infrastructure.Repository.Permission.Info.RoleInfo
{
    public interface IRoleInfoRepository : IRepository<Domain.Permission.Info.RoleInfo.RoleInfo>
    {
        /// <summary>
        /// ��ȡ��ɫ�µ������¼�
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        List<RoleInfoTreeOutputDto> GetChildren(string roleid);
    }
}