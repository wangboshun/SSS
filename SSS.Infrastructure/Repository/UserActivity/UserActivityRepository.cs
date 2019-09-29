using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;
using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.UserActivity
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserActivityRepository))]
    public class UserActivityRepository : Repository<SSS.Domain.UserActivity.UserActivity>, IUserActivityRepository
    {
        public UserActivityRepository(DbcontextBase context) : base(context)
        {
        }

        public bool AddActivity(List<Domain.UserActivity.UserActivity> list)
        {
            DbSet.AddRange(list);

            return SaveChanges() > 0;
        }
    }
}