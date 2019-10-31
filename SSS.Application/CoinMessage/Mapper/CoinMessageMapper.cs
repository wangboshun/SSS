using SSS.Domain.CoinMessage.Dto;

namespace SSS.Application.CoinMessage.Mapper
{
    public class CoinMessageProfile : AutoMapper.Profile
    {
        public CoinMessageProfile()
        {
            CreateMap<SSS.Domain.CoinMessage.CoinMessage, CoinMessageOutputDto>();

            CreateMap<CoinMessageInputDto, SSS.Domain.CoinMessage.CoinMessage>();
        }
    }
}
