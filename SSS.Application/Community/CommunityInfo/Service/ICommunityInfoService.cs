using SSS.Application.Seedwork.Service;
using SSS.Domain.Community.CommunityBusinessRelation.Dto;
using SSS.Domain.Community.CommunityInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Community.CommunityInfo.Service
{
    public interface ICommunityInfoService : IQueryService<Domain.Community.CommunityInfo.CommunityInfo, CommunityInfoInputDto, CommunityInfoOutputDto>
    {
        CommunityBusinessRelationOutputDto AddCommunityBusinessRelation(CommunityBusinessRelationInputDto input);

        CommunityInfoOutputDto AddCommunityInfo(CommunityInfoInputDto input);

        Pages<List<CommunityInfoOutputDto>> GetListCommunityInfo(CommunityInfoInputDto input);

        bool UpdateCommunityInfo(CommunityInfoInputDto input);
    }
}