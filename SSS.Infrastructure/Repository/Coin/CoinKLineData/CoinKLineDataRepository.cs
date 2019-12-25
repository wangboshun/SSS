using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Coin.CoinKLineData
{
    [DIService(ServiceLifetime.Scoped, typeof(ICoinKLineDataRepository))]
    public class CoinKLineDataRepository : Repository<SSS.Domain.Coin.CoinKLineData.CoinKLineData>, ICoinKLineDataRepository
    {
        public CoinKLineDataRepository(DbcontextBase context) : base(context)
        {
        }
    }
}