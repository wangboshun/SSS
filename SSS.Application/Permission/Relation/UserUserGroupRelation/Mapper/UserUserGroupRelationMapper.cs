using SSS.Domain.Permission.Relation.UserUserGroupRelation.Dto;

namespace SSS.Application.Permission.Relation.UserUserGroupRelation.Mapper
{
    public class UserUserGroupRelationProfile : AutoMapper.Profile
    {
        public UserUserGroupRelationProfile()
        {
            CreateMap<SSS.Domain.Permission.Relation.UserUserGroupRelation.UserUserGroupRelation, UserUserGroupRelationOutputDto>();

            CreateMap<UserUserGroupRelationInputDto, SSS.Domain.Permission.Relation.UserUserGroupRelation.UserUserGroupRelation>();
        }
    }
}
