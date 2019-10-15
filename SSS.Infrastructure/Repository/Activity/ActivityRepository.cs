using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Activity
{
    [DIService(ServiceLifetime.Scoped, typeof(IActivityRepository))]
    public class ActivityRepository : Repository<Domain.Activity.Activity>, IActivityRepository
    {
        public ActivityRepository(DbcontextBase context) : base(context)
        {
        }
    }
}