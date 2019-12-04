using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Relation.UserUserGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Relation.UserUserGroupRelation.Service
{
    public interface IUserUserGroupRelationService : IQueryService<SSS.Domain.Permission.Relation.UserUserGroupRelation.UserUserGroupRelation, UserUserGroupRelationInputDto, UserUserGroupRelationOutputDto>
    {
        void AddUserUserGroupRelation(UserUserGroupRelationInputDto input);

        Pages<List<UserUserGroupRelationOutputDto>> GetListUserUserGroupRelation(UserUserGroupRelationInputDto input);

        void DeleteUserUserGroupRelation(UserUserGroupRelationInputDto input);
    }
}