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
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<RoleGroupRelationOutputDto>> GetRoleGroupByRole(RoleGroupRelationInputDto input);

        /// <summary>
        /// ���ݽ�ɫ��Id�����ƣ�����������ɫ
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<RoleGroupRelationOutputDto>> GetRoleListGroupByGroup(RoleGroupRelationInputDto input);
    }
}