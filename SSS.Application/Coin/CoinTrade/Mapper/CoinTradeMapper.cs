using AutoMapper;

using SSS.Domain.Coin.CoinTrade.Dto;

namespace SSS.Application.Coin.CoinTrade.Mapper
{
    public class CoinTradeProfile : Profile
    {
        public CoinTradeProfile()
        {
            CreateMap<Domain.Coin.CoinTrade.CoinTrade, CoinTradeOutputDto>();

            CreateMap<CoinTradeInputDto, Domain.Coin.CoinTrade.CoinTrade>();
        }
    }
}