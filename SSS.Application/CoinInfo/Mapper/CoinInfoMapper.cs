using AutoMapper;

using SSS.Domain.CoinInfo.Dto;

namespace SSS.Application.CoinInfo.Mapper
{
    public class CoinInfoProfile : Profile
    {
        public CoinInfoProfile()
        {
            CreateMap<Domain.CoinInfo.CoinInfo, CoinInfoOutputDto>();

            CreateMap<CoinInfoInputDto, Domain.CoinInfo.CoinInfo>();
        }
    }
}