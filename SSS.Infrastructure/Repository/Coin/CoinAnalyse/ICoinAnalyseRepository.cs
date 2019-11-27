using SSS.Domain.Coin.CoinAnalyse.Dto;
using SSS.Domain.Seedwork.Repository;

using System.Linq;

namespace SSS.Infrastructure.Repository.Coin.CoinAnalyse
{
    public interface ICoinAnalyseRepository : IRepository<SSS.Domain.Coin.CoinAnalyse.CoinAnalyse>
    {
        IQueryable<Domain.Coin.CoinAnalyse.CoinAnalyse> GetPageOrderByAsc(CoinAnalyseInputDto input, ref int count);
    }
}