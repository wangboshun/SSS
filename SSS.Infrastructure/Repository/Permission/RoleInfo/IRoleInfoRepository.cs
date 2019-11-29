using SSS.Domain.Permission.RoleInfo.Dto;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.RoleInfo
{
    public interface IRoleInfoRepository : IRepository<SSS.Domain.Permission.RoleInfo.RoleInfo>
    {
        /// <summary>
        /// ��ȡ��ɫ�µ������¼�
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        List<RoleInfoTreeOutputDto> GetChildren(string roleid);
    }
}