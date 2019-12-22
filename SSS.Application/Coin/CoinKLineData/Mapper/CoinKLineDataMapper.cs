using SSS.Domain.Coin.CoinKLineData.Dto;

namespace SSS.Application.Coin.CoinKLineData.Mapper
{
    public class CoinKLineDataProfile : AutoMapper.Profile
    {
        public CoinKLineDataProfile()
        {
            CreateMap<SSS.Domain.Coin.CoinKLineData.CoinKLineData, CoinKLineDataOutputDto>();

            CreateMap<CoinKLineDataInputDto, SSS.Domain.Coin.CoinKLineData.CoinKLineData>();
        }
    }
}
