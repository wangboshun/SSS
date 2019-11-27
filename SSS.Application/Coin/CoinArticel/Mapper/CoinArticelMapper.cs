using AutoMapper;

using SSS.Domain.Coin.CoinArticel.Dto;

namespace SSS.Application.Coin.CoinArticel.Mapper
{
    public class CoinArticelProfile : Profile
    {
        public CoinArticelProfile()
        {
            CreateMap<Domain.Coin.CoinArticel.CoinArticel, CoinArticelOutputDto>();

            CreateMap<CoinArticelInputDto, Domain.Coin.CoinArticel.CoinArticel>();
        }
    }
}