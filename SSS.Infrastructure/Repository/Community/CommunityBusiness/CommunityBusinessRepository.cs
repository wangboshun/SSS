using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Community.CommunityBusiness
{
    [DIService(ServiceLifetime.Scoped, typeof(ICommunityBusinessRepository))]
    public class CommunityBusinessRepository : Repository<SSS.Domain.Community.CommunityBusiness.CommunityBusiness>, ICommunityBusinessRepository
    {
        public CommunityBusinessRepository(CommunityDbContext context) : base(context)
        {
        }
    }
}