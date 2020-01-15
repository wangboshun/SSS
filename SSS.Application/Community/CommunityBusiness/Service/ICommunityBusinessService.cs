using SSS.Application.Seedwork.Service;
using SSS.Domain.Community.CommunityBusiness.Dto;
using SSS.Domain.Community.CommunityInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Community.CommunityBusiness.Service
{
    public interface ICommunityBusinessService : IQueryService<Domain.Community.CommunityBusiness.CommunityBusiness, CommunityBusinessInputDto, CommunityBusinessOutputDto>
    {
        CommunityBusinessOutputDto AddCommunityBusiness(CommunityBusinessInputDto input);
        bool UpdateCommunityBusiness(CommunityBusinessInputDto input);
        Pages<List<CommunityBusinessOutputDto>> GetListCommunityBusiness(CommunityBusinessInputDto input);
        Pages<List<CommunityBusinessOutputDto>> GetCommunityBusinessByCommunity(CommunityInfoInputDto input);
    }
}