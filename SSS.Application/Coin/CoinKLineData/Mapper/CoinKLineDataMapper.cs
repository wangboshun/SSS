using AutoMapper;

using SSS.Domain.Coin.CoinKLineData.Dto;

namespace SSS.Application.Coin.CoinKLineData.Mapper
{
    public class CoinKLineDataProfile : Profile
    {
        public CoinKLineDataProfile()
        {
            CreateMap<Domain.Coin.CoinKLineData.CoinKLineData, CoinKLineDataOutputDto>();

            CreateMap<CoinKLineDataInputDto, Domain.Coin.CoinKLineData.CoinKLineData>();
        }
    }
}