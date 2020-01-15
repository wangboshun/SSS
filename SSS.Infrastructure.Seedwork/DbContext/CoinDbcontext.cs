using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

using SSS.Domain.Coin.CoinAnalyse;
using SSS.Domain.Coin.CoinArticel;
using SSS.Domain.Coin.CoinInfo;
using SSS.Domain.Coin.CoinKLineData;
using SSS.Domain.Coin.CoinMessage;
using SSS.Domain.Coin.CoinTrade;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Seedwork.DbContext
{
    [DIService(ServiceLifetime.Scoped, typeof(CoinDbContext))]
    public class CoinDbContext : DbContextBase
    {
        public CoinDbContext(IHostEnvironment env, ILoggerFactory factory) : base(env, factory)
        {
        }

        #region Coin

        public DbSet<CoinArticel> CoinArticel { get; set; }
        public DbSet<CoinAnalyse> CoinAnalyse { get; set; }
        public DbSet<CoinInfo> CoinInfo { get; set; }
        public DbSet<CoinMessage> CoinMessage { get; set; }
        public DbSet<CoinTrade> CoinTrade { set; get; }

        public DbSet<CoinKLineData> CoinKLineData { set; get; }

        #endregion
    }
}