using SSS.Application.Seedwork.Service;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Trade.Dto;

using System.Collections.Generic;

namespace SSS.Application.Trade.Service
{
    public interface ITradeService : IQueryService<SSS.Domain.Trade.Trade, TradeInputDto, TradeOutputDto>
    {
        void AddTrade(TradeInputDto input);

        Pages<List<TradeOutputDto>> GetListTrade(TradeInputDto input);
    }
}