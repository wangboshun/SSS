using SSS.Domain.Coin.CoinMessage.Dto;
using SSS.Domain.Seedwork.Repository;

using System.Linq;

namespace SSS.Infrastructure.Repository.Coin.CoinMessage
{
    public interface ICoinMessageRepository : IRepository<Domain.Coin.CoinMessage.CoinMessage>
    {
        IQueryable<Domain.Coin.CoinMessage.CoinMessage> GetPageOrderByAsc(CoinMessageInputDto input, ref int count);
    }
}