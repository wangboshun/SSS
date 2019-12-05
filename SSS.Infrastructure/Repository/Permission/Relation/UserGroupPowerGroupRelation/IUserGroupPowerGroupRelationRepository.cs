using SSS.Domain.Permission.Relation.UserGroupPowerGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.UserGroupPowerGroupRelation
{
    public interface IUserGroupPowerGroupRelationRepository : IRepository<SSS.Domain.Permission.Relation.UserGroupPowerGroupRelation.UserGroupPowerGroupRelation>
    {
        /// <summary>
        /// ����Ȩ����Id�����ƣ����������û���
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserGroupPowerGroupRelationOutputDto>> GetUserGroupByPowerGroup(UserGroupPowerGroupRelationInputDto input);

        /// <summary>
        /// �����û���Id�����ƣ���������Ȩ����
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserGroupPowerGroupRelationOutputDto>> GetPowerGroupByUserGroup(UserGroupPowerGroupRelationInputDto input);
    }
}