using SSS.Application.Seedwork.Service;
using SSS.Domain.Coin.CoinKLineData.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Coin.CoinKLineData.Service
{
    public interface ICoinKLineDataService : IQueryService<SSS.Domain.Coin.CoinKLineData.CoinKLineData, CoinKLineDataInputDto, CoinKLineDataOutputDto>
    {
        CoinKLineDataOutputDto AddCoinKLineData(CoinKLineDataInputDto input);

        Pages<List<CoinKLineDataOutputDto>> GetListCoinKLineData(CoinKLineDataInputDto input);
    }
}