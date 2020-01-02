using Microsoft.Extensions.DependencyInjection;

using Quartz;
using Quartz.Spi;

using SSS.Infrastructure.Util.Attribute;

using System;

namespace SSS.Application.Job.Factory
{

    /// <summary>
    /// JobFactory
    /// </summary>
    [DIService(ServiceLifetime.Singleton, typeof(IJobFactory))]
    public class JobFactory : IJobFactory
    {
        private readonly IServiceProvider _serviceProvider;

        /// <summary>
        /// JobFactory
        /// </summary>
        /// <param name="serviceProvider"></param>
        public JobFactory(IServiceProvider serviceProvider)
        {
            _serviceProvider = serviceProvider;
        }

        public IJob NewJob(TriggerFiredBundle bundle, IScheduler scheduler)
        {
            return _serviceProvider.GetService(bundle.JobDetail.JobType) as IJob;
        }

        public void ReturnJob(IJob job)
        {
            var disposable = job as IDisposable;
            disposable?.Dispose();

        }
    }
}