using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Coin.CoinTrade
{
    [DIService(ServiceLifetime.Scoped, typeof(ICoinTradeRepository))]
    public class CoinTradeRepository : Repository<Domain.Coin.CoinTrade.CoinTrade>, ICoinTradeRepository
    {
        public CoinTradeRepository(DbcontextBase context) : base(context)
        {
        }
    }
}