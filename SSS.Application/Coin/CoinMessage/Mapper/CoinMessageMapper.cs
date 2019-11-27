using AutoMapper;

using SSS.Domain.Coin.CoinMessage.Dto;

namespace SSS.Application.Coin.CoinMessage.Mapper
{
    public class CoinMessageProfile : Profile
    {
        public CoinMessageProfile()
        {
            CreateMap<Domain.Coin.CoinMessage.CoinMessage, CoinMessageOutputDto>();

            CreateMap<CoinMessageInputDto, Domain.Coin.CoinMessage.CoinMessage>();
        }
    }
}