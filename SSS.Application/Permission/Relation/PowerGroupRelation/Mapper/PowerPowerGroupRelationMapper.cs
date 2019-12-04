using SSS.Domain.Permission.Relation.PowerGroupRelation.Dto;

namespace SSS.Application.Permission.Relation.PowerGroupRelation.Mapper
{
    public class PowerGroupRelationProfile : AutoMapper.Profile
    {
        public PowerGroupRelationProfile()
        {
            CreateMap<SSS.Domain.Permission.Relation.PowerGroupRelation.PowerGroupRelation, PowerGroupRelationOutputDto>();

            CreateMap<PowerGroupRelationInputDto, SSS.Domain.Permission.Relation.PowerGroupRelation.PowerGroupRelation>();
        }
    }
}
