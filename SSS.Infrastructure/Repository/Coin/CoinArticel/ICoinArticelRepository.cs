using SSS.Domain.Coin.CoinArticel.Dto;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.CoinArticel
{
    public interface ICoinArticelRepository : IRepository<Domain.Coin.CoinArticel.CoinArticel>
    {
        IEnumerable<Domain.Coin.CoinArticel.CoinArticel> GetNews(CoinArticelInputDto input);
    }
}