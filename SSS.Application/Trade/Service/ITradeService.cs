using SSS.Domain.Trade.Dto;
using SSS.Domain.Seedwork.Model;
using System.Collections.Generic;
using SSS.Application.Seedwork.Service;

namespace SSS.Application.Trade.Service
{
    public interface ITradeService : IQueryService<SSS.Domain.Trade.Trade, TradeInputDto, TradeOutputDto>
    {
        void AddTrade(TradeInputDto input);

		Pages<List<TradeOutputDto>> GetListTrade(TradeInputDto input);
    }
}