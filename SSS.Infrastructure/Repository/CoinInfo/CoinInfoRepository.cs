using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;

namespace SSS.Infrastructure.Repository.CoinInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(ICoinInfoRepository))]
    public class CoinInfoRepository : Repository<SSS.Domain.CoinInfo.CoinInfo>, ICoinInfoRepository
    {
        public CoinInfoRepository(DbcontextBase context) : base(context)
        {
        }
    }
}