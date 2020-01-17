using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Community.CommunityTask
{
    [DIService(ServiceLifetime.Scoped, typeof(ICommunityTaskRepository))]
    public class CommunityTaskRepository : Repository<SSS.Domain.Community.CommunityTask.CommunityTask>, ICommunityTaskRepository
    {
        public CommunityTaskRepository(CommunityDbContext context) : base(context)
        {
        }
    }
}