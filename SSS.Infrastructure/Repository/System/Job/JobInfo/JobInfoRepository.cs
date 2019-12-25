using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;

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