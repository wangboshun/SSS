using SSS.Domain.Seedwork.Repository;
using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.UserActivity
{
    public interface IUserActivityRepository : IRepository<SSS.Domain.UserActivity.UserActivity>
    {
        bool AddActivity(List<Domain.UserActivity.UserActivity> list);
    }
}