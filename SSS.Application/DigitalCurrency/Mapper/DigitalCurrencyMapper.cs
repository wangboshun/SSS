using SSS.Domain.DigitalCurrency.Dto;

namespace SSS.Application.DigitalCurrency.Mapper
{
    public class DigitalCurrencyProfile : AutoMapper.Profile
    {
        public DigitalCurrencyProfile()
        {
            CreateMap<SSS.Domain.DigitalCurrency.DigitalCurrency, DigitalCurrencyOutputDto>();

            CreateMap<DigitalCurrencyInputDto, SSS.Domain.DigitalCurrency.DigitalCurrency>();
        }
    }
}
