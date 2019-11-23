using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;

namespace SSS.Infrastructure.Repository.Trade
{
    [DIService(ServiceLifetime.Scoped, typeof(ITradeRepository))]
    public class TradeRepository : Repository<SSS.Domain.Trade.Trade>, ITradeRepository
    {
        public TradeRepository(DbcontextBase context) : base(context)
        {
        }
    }
}