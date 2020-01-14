using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.System.Job.JobError
{
    [DIService(ServiceLifetime.Scoped, typeof(IJobErrorRepository))]
    public class JobErrorRepository : Repository<SSS.Domain.System.Job.JobError.JobError>, IJobErrorRepository
    {
        public JobErrorRepository(SystemDbcontext context) : base(context)
        {
        }
    }
}