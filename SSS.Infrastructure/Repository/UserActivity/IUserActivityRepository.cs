using System.Collections.Generic;
using SSS.Domain.Seedwork.Repository;

namespace SSS.Infrastructure.Repository.UserActivity
{
    public interface IUserActivityRepository : IRepository<Domain.UserActivity.UserActivity>
    {
        bool AddActivity(List<Domain.UserActivity.UserActivity> list);
    }
}