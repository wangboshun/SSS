using System.Collections.Generic;
using SSS.Domain.Coin.CoinArticel.Dto;
using SSS.Domain.Seedwork.Repository;

namespace SSS.Infrastructure.Repository.Coin.CoinArticel
{
    public interface ICoinArticelRepository : IRepository<Domain.Coin.CoinArticel.CoinArticel>
    {
        IEnumerable<Domain.Coin.CoinArticel.CoinArticel> GetNews(CoinArticelInputDto input);
    }
}