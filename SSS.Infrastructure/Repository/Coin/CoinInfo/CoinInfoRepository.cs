using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Coin.CoinInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(ICoinInfoRepository))]
    public class CoinInfoRepository : Repository<Domain.Coin.CoinInfo.CoinInfo>, ICoinInfoRepository
    {
        public CoinInfoRepository(CoinDbContext context) : base(context)
        {
        }
    }
}