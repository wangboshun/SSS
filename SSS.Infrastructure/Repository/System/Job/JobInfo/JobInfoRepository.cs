using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.System.Job.JobInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(IJobInfoRepository))]
    public class JobInfoRepository : Repository<SSS.Domain.System.Job.JobInfo.JobInfo>, IJobInfoRepository
    {
        public JobInfoRepository(DbcontextBase context) : base(context)
        {
        }
    }
}