using SSS.Domain.Community.CommunityBusiness.Dto;

namespace SSS.Application.Community.CommunityBusiness.Mapper
{
    public class CommunityBusinessProfile : AutoMapper.Profile
    {
        public CommunityBusinessProfile()
        {
            CreateMap<SSS.Domain.Community.CommunityBusiness.CommunityBusiness, CommunityBusinessOutputDto>();

            CreateMap<CommunityBusinessInputDto, SSS.Domain.Community.CommunityBusiness.CommunityBusiness>();
        }
    }
}
