using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Relation.UserGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Relation.UserGroupRelation.Service
{
    public interface IUserGroupRelationService : IQueryService<SSS.Domain.Permission.Relation.UserGroupRelation.UserGroupRelation, UserGroupRelationInputDto, UserGroupRelationOutputDto>
    {
        void AddUserGroupRelation(UserGroupRelationInputDto input);

        Pages<List<UserGroupRelationOutputDto>> GetListUserGroupRelation(UserGroupRelationInputDto input);

        void DeleteUserGroupRelation(UserGroupRelationInputDto input);
    }
}