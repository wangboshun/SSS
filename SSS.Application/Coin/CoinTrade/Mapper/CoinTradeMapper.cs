using SSS.Domain.Coin.CoinTrade.Dto;

namespace SSS.Application.Coin.CoinTrade.Mapper
{
    public class CoinTradeProfile : AutoMapper.Profile
    {
        public CoinTradeProfile()
        {
            CreateMap<SSS.Domain.Coin.CoinTrade.CoinTrade, CoinTradeOutputDto>();

            CreateMap<CoinTradeInputDto, SSS.Domain.Coin.CoinTrade.CoinTrade>();
        }
    }
}
