using AutoMapper;

using SSS.Domain.Coin.CoinAnalyse.Dto;

namespace SSS.Application.Coin.CoinAnalyse.Mapper
{
    public class CoinAnalyseProfile : Profile
    {
        public CoinAnalyseProfile()
        {
            CreateMap<Domain.Coin.CoinAnalyse.CoinAnalyse, CoinAnalyseOutputDto>();

            CreateMap<CoinAnalyseInputDto, Domain.Coin.CoinAnalyse.CoinAnalyse>();
        }
    }
}