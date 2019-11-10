using AutoMapper;
using SSS.Domain.CoinMessage.Dto;

namespace SSS.Application.CoinMessage.Mapper
{
    public class CoinMessageProfile : Profile
    {
        public CoinMessageProfile()
        {
            CreateMap<Domain.CoinMessage.CoinMessage, CoinMessageOutputDto>();

            CreateMap<CoinMessageInputDto, Domain.CoinMessage.CoinMessage>();
        }
    }
}