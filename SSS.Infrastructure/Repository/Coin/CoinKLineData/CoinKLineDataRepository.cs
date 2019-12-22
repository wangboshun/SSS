using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;

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