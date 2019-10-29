using SSS.Domain.CoinInfo.Dto;

namespace SSS.Application.CoinInfo.Mapper
{
    public class CoinInfoProfile : AutoMapper.Profile
    {
        public CoinInfoProfile()
        {
            CreateMap<SSS.Domain.CoinInfo.CoinInfo, CoinInfoOutputDto>();

            CreateMap<CoinInfoInputDto, SSS.Domain.CoinInfo.CoinInfo>();
        }
    }
}
