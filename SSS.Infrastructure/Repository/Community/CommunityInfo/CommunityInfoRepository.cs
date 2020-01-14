using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Community.CommunityInfo
{
    [DIService(ServiceLifetime.Singleton, typeof(ICommunityInfoRepository))]
    public class CommunityInfoRepository : Repository<SSS.Domain.Community.CommunityInfo.CommunityInfo>, ICommunityInfoRepository
    {
        public CommunityInfoRepository(CommunityDbContext context) : base(context)
        {
        }
    }
}