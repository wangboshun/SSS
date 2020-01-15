using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

using SSS.Domain.Community.CommunityBusiness;
using SSS.Domain.Community.CommunityBusinessRelation;
using SSS.Domain.Community.CommunityInfo;
using SSS.Domain.Community.UserCommunityRelation;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Seedwork.DbContext
{
    [DIService(ServiceLifetime.Scoped, typeof(CommunityDbContext))]
    public class CommunityDbContext : DbContextBase
    {
        public CommunityDbContext(IHostEnvironment env, ILoggerFactory factory) : base(env, factory)
        {
        }

        #region Coin

        public DbSet<CommunityInfo> CommunityInfo { get; set; }
        public DbSet<CommunityBusiness> CommunityBusiness { get; set; }
        public DbSet<CommunityBusinessRelation> CommunityBusinessRelation { get; set; }
        public DbSet<UserCommunityRelation> UserCommunityRelation { get; set; }

        #endregion
    }
}