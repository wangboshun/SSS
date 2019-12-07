using SSS.Domain.Permission.Relation.UserPowerGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.UserPowerGroupRelation
{
    public interface IUserPowerGroupRelationRepository : IRepository<SSS.Domain.Permission.Relation.UserPowerGroupRelation.UserPowerGroupRelation>
    {
        /// <summary>
        /// ����Ȩ����Id�����ƣ����������û�
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserPowerGroupRelationOutputDto>> GetUserByPowerGroup(UserPowerGroupRelationInputDto input);

        /// <summary>
        /// �����û�Id�����ƣ���������Ȩ����
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserPowerGroupRelationOutputDto>> GetPowerGroupByUser(UserPowerGroupRelationInputDto input);
    }
}