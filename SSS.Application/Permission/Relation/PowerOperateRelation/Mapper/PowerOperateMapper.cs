using AutoMapper;
using SSS.Domain.Permission.Relation.PowerOperateRelation.Dto;

namespace SSS.Application.Permission.Relation.PowerOperateRelation.Mapper
{
    public class PowerOperateRelationProfile : Profile
    {
        public PowerOperateRelationProfile()
        {
            CreateMap<Domain.Permission.Relation.PowerOperateRelation.PowerOperateRelation, PowerOperateRelationOutputDto>();

            CreateMap<PowerOperateRelationInputDto, Domain.Permission.Relation.PowerOperateRelation.PowerOperateRelation>();
        }
    }
}