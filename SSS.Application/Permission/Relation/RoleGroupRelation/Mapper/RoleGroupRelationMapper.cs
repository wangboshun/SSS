using SSS.Domain.Permission.Relation.RoleGroupRelation.Dto;

namespace SSS.Application.Permission.Relation.RoleGroupRelation.Mapper
{
    public class RoleGroupRelationProfile : AutoMapper.Profile
    {
        public RoleGroupRelationProfile()
        {
            CreateMap<SSS.Domain.Permission.Relation.RoleGroupRelation.RoleGroupRelation, RoleGroupRelationOutputDto>();

            CreateMap<RoleGroupRelationInputDto, SSS.Domain.Permission.Relation.RoleGroupRelation.RoleGroupRelation>();
        }
    }
}
