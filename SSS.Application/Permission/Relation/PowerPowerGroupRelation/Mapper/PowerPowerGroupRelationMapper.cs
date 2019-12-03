using SSS.Domain.Permission.Relation.PowerPowerGroupRelation.Dto;

namespace SSS.Application.Permission.Relation.PowerPowerGroupRelation.Mapper
{
    public class PowerPowerGroupRelationProfile : AutoMapper.Profile
    {
        public PowerPowerGroupRelationProfile()
        {
            CreateMap<SSS.Domain.Permission.Relation.PowerPowerGroupRelation.PowerPowerGroupRelation, PowerPowerGroupRelationOutputDto>();

            CreateMap<PowerPowerGroupRelationInputDto, SSS.Domain.Permission.Relation.PowerPowerGroupRelation.PowerPowerGroupRelation>();
        }
    }
}
