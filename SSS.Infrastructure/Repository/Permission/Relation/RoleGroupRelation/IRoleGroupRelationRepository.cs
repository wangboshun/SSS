using SSS.Domain.Permission.Relation.RoleGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.RoleGroupRelation
{
    public interface IRoleGroupRelationRepository : IRepository<SSS.Domain.Permission.Relation.RoleGroupRelation.RoleGroupRelation>
    {
        /// <summary>
        /// ���ݽ�ɫId�����ƣ�����������ɫ��
        /// </summary> 
        /// <returns></returns>
        Pages<List<RoleGroupRelationOutputDto>> GetRoleGroupByRole(string roleid, string rolename, string parentid = "", int pageindex = 0, int pagesize = 0);

        /// <summary>
        /// ���ݽ�ɫ��Id�����ƣ�����������ɫ
        /// </summary> 
        /// <returns></returns>
        Pages<List<RoleGroupRelationOutputDto>> GetRoleByRoleGroup(string rolegroupid, string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0);
    }
}