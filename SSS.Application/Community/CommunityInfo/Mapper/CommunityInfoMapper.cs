using AutoMapper;

using SSS.Domain.Community.CommunityBusinessRelation;
using SSS.Domain.Community.CommunityBusinessRelation.Dto;
using SSS.Domain.Community.CommunityInfo.Dto;

namespace SSS.Application.Community.CommunityInfo.Mapper
{
    public class CommunityInfoProfile : Profile
    {
        public CommunityInfoProfile()
        {
            CreateMap<Domain.Community.CommunityInfo.CommunityInfo, CommunityInfoOutputDto>();

            CreateMap<CommunityInfoInputDto, Domain.Community.CommunityInfo.CommunityInfo>();

            CreateMap<CommunityBusinessRelation, CommunityBusinessRelationOutputDto>();

            CreateMap<CommunityBusinessRelationInputDto, CommunityBusinessRelation>();
        }
    }
}