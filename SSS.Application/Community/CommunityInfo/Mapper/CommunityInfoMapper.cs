using SSS.Domain.Community.CommunityBusinessRelation.Dto;
using SSS.Domain.Community.CommunityInfo.Dto;

namespace SSS.Application.Community.CommunityInfo.Mapper
{
    public class CommunityInfoProfile : AutoMapper.Profile
    {
        public CommunityInfoProfile()
        {
            CreateMap<SSS.Domain.Community.CommunityInfo.CommunityInfo, CommunityInfoOutputDto>();

            CreateMap<CommunityInfoInputDto, SSS.Domain.Community.CommunityInfo.CommunityInfo>();

            CreateMap<SSS.Domain.Community.CommunityBusinessRelation.CommunityBusinessRelation, CommunityBusinessRelationOutputDto>();

            CreateMap<CommunityBusinessRelationInputDto, SSS.Domain.Community.CommunityBusinessRelation.CommunityBusinessRelation>();
        }
    }
}
