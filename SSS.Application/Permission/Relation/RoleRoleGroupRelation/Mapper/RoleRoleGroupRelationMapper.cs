using SSS.Domain.Permission.Relation.RoleRoleGroupRelation.Dto;

namespace SSS.Application.Permission.Relation.RoleRoleGroupRelation.Mapper
{
    public class RoleRoleGroupRelationProfile : AutoMapper.Profile
    {
        public RoleRoleGroupRelationProfile()
        {
            CreateMap<SSS.Domain.Permission.Relation.RoleRoleGroupRelation.RoleRoleGroupRelation, RoleRoleGroupRelationOutputDto>();

            CreateMap<RoleRoleGroupRelationInputDto, SSS.Domain.Permission.Relation.RoleRoleGroupRelation.RoleRoleGroupRelation>();
        }
    }
}
