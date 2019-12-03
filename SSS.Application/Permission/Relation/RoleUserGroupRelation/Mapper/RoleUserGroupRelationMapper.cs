using SSS.Domain.Permission.Relation.RoleUserGroupRelation.Dto;

namespace SSS.Application.Permission.Relation.RoleUserGroupRelation.Mapper
{
    public class RoleUserGroupRelationProfile : AutoMapper.Profile
    {
        public RoleUserGroupRelationProfile()
        {
            CreateMap<SSS.Domain.Permission.Relation.RoleUserGroupRelation.RoleUserGroupRelation, RoleUserGroupRelationOutputDto>();

            CreateMap<RoleUserGroupRelationInputDto, SSS.Domain.Permission.Relation.RoleUserGroupRelation.RoleUserGroupRelation>();
        }
    }
}
