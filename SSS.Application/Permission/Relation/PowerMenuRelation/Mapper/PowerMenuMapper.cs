using AutoMapper;
using SSS.Domain.Permission.Relation.PowerMenuRelation.Dto;

namespace SSS.Application.Permission.Relation.PowerMenu.Mapper
{
    public class PowerMenuRelationProfile : Profile
    {
        public PowerMenuRelationProfile()
        {
            CreateMap<Domain.Permission.Relation.PowerMenuRelation.PowerMenuRelation, PowerMenuRelationOutputDto>();

            CreateMap<PowerMenuRelationInputDto, Domain.Permission.Relation.PowerMenuRelation.PowerMenuRelation>();
        }
    }
}