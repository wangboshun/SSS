using SSS.Domain.Permission.Relation.UserGroupRelation.Dto;

namespace SSS.Application.Permission.Relation.UserGroupRelation.Mapper
{
    public class UserGroupRelationProfile : AutoMapper.Profile
    {
        public UserGroupRelationProfile()
        {
            CreateMap<SSS.Domain.Permission.Relation.UserGroupRelation.UserGroupRelation, UserGroupRelationOutputDto>();

            CreateMap<UserGroupRelationInputDto, SSS.Domain.Permission.Relation.UserGroupRelation.UserGroupRelation>();
        }
    }
}
