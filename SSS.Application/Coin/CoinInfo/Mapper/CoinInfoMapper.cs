using AutoMapper;

using SSS.Domain.Coin.CoinInfo.Dto;

namespace SSS.Application.Coin.CoinInfo.Mapper
{
    public class CoinInfoProfile : Profile
    {
        public CoinInfoProfile()
        {
            CreateMap<Domain.Coin.CoinInfo.CoinInfo, CoinInfoOutputDto>();

            CreateMap<CoinInfoInputDto, Domain.Coin.CoinInfo.CoinInfo>();
        }
    }
}