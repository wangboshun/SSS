using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Trade
{
    [DIService(ServiceLifetime.Scoped, typeof(ITradeRepository))]
    public class TradeRepository : Repository<Domain.Trade.Trade>, ITradeRepository
    {
        public TradeRepository(DbcontextBase context) : base(context)
        {
        }
    }
}