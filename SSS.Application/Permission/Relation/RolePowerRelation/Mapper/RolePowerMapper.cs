using AutoMapper;
using SSS.Domain.Permission.Relation.RolePowerRelation.Dto;

namespace SSS.Application.Permission.Relation.RolePowerRelation.Mapper
{
    public class RolePowerRelationProfile : Profile
    {
        public RolePowerRelationProfile()
        {
            CreateMap<Domain.Permission.Relation.RolePowerRelation.RolePowerRelation, RolePowerRelationOutputDto>();

            CreateMap<RolePowerRelationInputDto, Domain.Permission.Relation.RolePowerRelation.RolePowerRelation>();
        }
    }
}