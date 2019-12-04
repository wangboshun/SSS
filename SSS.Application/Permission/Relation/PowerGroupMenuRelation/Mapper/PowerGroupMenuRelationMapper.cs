using AutoMapper;

using SSS.Domain.Permission.Relation.PowerGroupMenuRelation.Dto;

namespace SSS.Application.Permission.Relation.PowerGroupMenu.Mapper
{
    public class PowerGroupMenuRelationProfile : Profile
    {
        public PowerGroupMenuRelationProfile()
        {
            CreateMap<Domain.Permission.Relation.PowerGroupMenuRelation.PowerGroupMenuRelation, PowerGroupMenuRelationOutputDto>();

            CreateMap<PowerGroupMenuRelationInputDto, Domain.Permission.Relation.PowerGroupMenuRelation.PowerGroupMenuRelation>();
        }
    }
}