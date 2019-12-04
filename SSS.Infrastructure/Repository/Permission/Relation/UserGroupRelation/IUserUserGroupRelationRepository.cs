using SSS.Domain.Permission.Relation.UserGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Permission.Relation.UserGroupRelation
{
    public interface IUserGroupRelationRepository : IRepository<SSS.Domain.Permission.Relation.UserGroupRelation.UserGroupRelation>
    {
        /// <summary>
        /// �����û���Id�����ƣ����������û�
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserGroupRelationOutputDto>> GetUserListByGroup(UserGroupRelationInputDto input);

        /// <summary>
        /// �����û�Id�����ƣ����������û���
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserGroupRelationOutputDto>> GetUserGroupByUser(UserGroupRelationInputDto input);
    }
}