using SSS.Domain.Permission.Relation.RoleRoleGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.RoleRoleGroupRelation
{
    public interface IRoleRoleGroupRelationRepository : IRepository<SSS.Domain.Permission.Relation.RoleRoleGroupRelation.RoleRoleGroupRelation>
    {
        /// <summary>
        /// ���ݽ�ɫId�����ƣ�����������ɫ��
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<RoleRoleGroupRelationOutputDto>> GetRoleGroupByRole(RoleRoleGroupRelationInputDto input);

        /// <summary>
        /// ���ݽ�ɫ��Id�����ƣ�����������ɫ
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<RoleRoleGroupRelationOutputDto>> GetRoleListGroupByGroup(RoleRoleGroupRelationInputDto input);
    }
}