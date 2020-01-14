using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

using SSS.Domain.System.Job.JobError;
using SSS.Domain.System.Job.JobInfo;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Seedwork.DbContext
{
    [DIService(ServiceLifetime.Scoped, typeof(SystemDbContext))]
    public class SystemDbContext : DbContextBase
    {
        public SystemDbContext(IHostEnvironment env, ILoggerFactory factory) : base(env, factory)
        {
        }

        #region System

        public DbSet<JobInfo> JobInfo { get; set; }

        public DbSet<JobError> JobError { get; set; }

        #endregion
    }
}