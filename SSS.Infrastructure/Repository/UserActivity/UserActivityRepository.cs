using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;

namespace SSS.Infrastructure.Repository.UserActivity
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserActivityRepository))]
    public class UserActivityRepository : Repository<SSS.Domain.UserActivity.UserActivity>, IUserActivityRepository
    {
        public UserActivityRepository(DbcontextBase context) : base(context)
        {
        }
    }
}