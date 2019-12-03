using SSS.Application.Seedwork.Service;
using SSS.Domain.Coin.CoinArticel.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Coin.CoinArticel.Service
{
    public interface ICoinArticelService : IQueryService<Domain.Coin.CoinArticel.CoinArticel, CoinArticelInputDto,
        CoinArticelOutputDto>
    {
        void AddCoinArticel(CoinArticelInputDto input);

        Pages<List<CoinArticelOutputDto>> GetListCoinArticel(CoinArticelInputDto input);

        List<CoinArticelOutputDto> GetNews(CoinArticelInputDto input);
    }
}