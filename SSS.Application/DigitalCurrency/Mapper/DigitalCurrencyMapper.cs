using AutoMapper;
using SSS.Domain.DigitalCurrency.Dto;

namespace SSS.Application.DigitalCurrency.Mapper
{
    public class DigitalCurrencyProfile : Profile
    {
        public DigitalCurrencyProfile()
        {
            CreateMap<Domain.DigitalCurrency.DigitalCurrency, DigitalCurrencyOutputDto>();

            CreateMap<DigitalCurrencyInputDto, Domain.DigitalCurrency.DigitalCurrency>();
        }
    }
}