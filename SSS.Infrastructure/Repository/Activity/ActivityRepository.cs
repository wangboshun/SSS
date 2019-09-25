using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;

namespace SSS.Infrastructure.Repository.Activity
{
    [DIService(ServiceLifetime.Scoped, typeof(IActivityRepository))]
    public class ActivityRepository : Repository<SSS.Domain.Activity.Activity>, IActivityRepository
    {
        public ActivityRepository(DbcontextBase context) : base(context)
        {
        }
    }
}