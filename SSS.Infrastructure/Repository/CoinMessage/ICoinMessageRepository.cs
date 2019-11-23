using SSS.Domain.CoinMessage.Dto;
using SSS.Domain.Seedwork.Repository;

using System.Linq;

namespace SSS.Infrastructure.Repository.CoinMessage
{
    public interface ICoinMessageRepository : IRepository<SSS.Domain.CoinMessage.CoinMessage>
    {
        IQueryable<Domain.CoinMessage.CoinMessage> GetPageOrderByAsc(CoinMessageInputDto input, ref int count);
    }
}