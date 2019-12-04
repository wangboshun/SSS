using AutoMapper;

using SSS.Domain.Permission.Relation.PowerGroupOperateRelation.Dto;

namespace SSS.Application.Permission.Relation.PowerGroupOperateRelation.Mapper
{
    public class PowerGroupOperateRelationProfile : Profile
    {
        public PowerGroupOperateRelationProfile()
        {
            CreateMap<Domain.Permission.Relation.PowerGroupOperateRelation.PowerGroupOperateRelation, PowerGroupOperateRelationOutputDto>();

            CreateMap<PowerGroupOperateRelationInputDto, Domain.Permission.Relation.PowerGroupOperateRelation.PowerGroupOperateRelation>();
        }
    }
}