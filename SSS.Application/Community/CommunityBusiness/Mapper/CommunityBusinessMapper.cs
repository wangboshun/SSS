using AutoMapper;

using SSS.Domain.Community.CommunityBusiness.Dto;

namespace SSS.Application.Community.CommunityBusiness.Mapper
{
    public class CommunityBusinessProfile : Profile
    {
        public CommunityBusinessProfile()
        {
            CreateMap<Domain.Community.CommunityBusiness.CommunityBusiness, CommunityBusinessOutputDto>();

            CreateMap<CommunityBusinessInputDto, Domain.Community.CommunityBusiness.CommunityBusiness>();
        }
    }
}