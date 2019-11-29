using SSS.Domain.Permission.UserRole.Dto;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.UserRole
{
    public interface IUserRoleRepository : IRepository<SSS.Domain.Permission.UserRole.UserRole>
    {
        /// <summary>
        /// ��ȡ��ɫ�������û���Ϣ
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        List<UserRoleOutputDto> GetUserRoleByRole(string roleid);

        /// <summary>
        /// �����û�����ȡ��ɫ��Ϣ
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        SSS.Domain.Permission.RoleInfo.RoleInfo GetRoleByUser(string userid);

        /// <summary>
        /// ɾ����ɫ�µ������û�
        /// </summary>
        /// <param name="roleid"></param>
        bool DeleteUserRoleByRole(string roleid);
    }
}