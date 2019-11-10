using System.Collections.Generic;
using SSS.Application.Seedwork.Service;
using SSS.Domain.DigitalCurrency.Dto;
using SSS.Domain.Seedwork.Model;

namespace SSS.Application.DigitalCurrency.Service
{
    public interface IDigitalCurrencyService : IQueryService<Domain.DigitalCurrency.DigitalCurrency,
        DigitalCurrencyInputDto, DigitalCurrencyOutputDto>
    {
        void AddDigitalCurrency(DigitalCurrencyInputDto input);

        Pages<List<DigitalCurrencyOutputDto>> GetListDigitalCurrency(DigitalCurrencyInputDto input);
    }
}