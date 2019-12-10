using SSS.Application.Seedwork.Service;
using SSS.Domain.Coin.CoinTrade.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Coin.CoinTrade.Service
{
    public interface
        ICoinTradeService : IQueryService<Domain.Coin.CoinTrade.CoinTrade, CoinTradeInputDto, CoinTradeOutputDto>
    {
        bool AddCoinTrade(CoinTradeInputDto input);

        Pages<List<CoinTradeOutputDto>> GetListCoinTrade(CoinTradeInputDto input);
    }
}