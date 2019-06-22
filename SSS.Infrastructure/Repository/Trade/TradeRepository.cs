using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;
using System.Linq;

namespace SSS.Infrastructure.Repository.Trade
{
    [DIService(ServiceLifetime.Scoped, typeof(ITradeRepository))]
    public class TradeRepository : Repository<SSS.Domain.Trade.Trade>, ITradeRepository
    {
        public TradeRepository(DbcontextBase context) : base(context)
        {
        }

        public Domain.Trade.Trade GetByTradeNo(string tradeno)
        {
            return DbSet.AsNoTracking().FirstOrDefault(x => x.First_Trade_No.Contains(tradeno));
        }
    }
}