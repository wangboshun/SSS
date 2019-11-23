using SSS.Domain.Trade.Dto;

namespace SSS.Application.Trade.Mapper
{
    public class TradeProfile : AutoMapper.Profile
    {
        public TradeProfile()
        {
            CreateMap<SSS.Domain.Trade.Trade, TradeOutputDto>();

            CreateMap<TradeInputDto, SSS.Domain.Trade.Trade>();
        }
    }
}
