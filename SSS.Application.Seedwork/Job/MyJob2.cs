using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

using Quartz;

using SSS.Infrastructure.Util.Attribute;

using System;
using System.Globalization;
using System.Threading.Tasks;

namespace SSS.Application.Seedwork.Job
{
    /// <summary>
    /// MyJob2
    /// </summary>
    [DIService(ServiceLifetime.Transient, typeof(MyJob2))]
    public class MyJob2 : IJobBase
    {
        private readonly ILogger<MyJob2> _logger;

        /// <summary>
        /// MyJob2
        /// </summary>
        /// <param name="logger"></param>
        public MyJob2(ILogger<MyJob2> logger)
        {
            _logger = logger;
        }
        /// <summary>
        /// Execute
        /// </summary>
        /// <param name="context"></param>
        /// <returns></returns>
        public Task Execute(IJobExecutionContext context)
        { 
            JobKey key = context.JobDetail.Key; 
            JobDataMap dataMap = context.JobDetail.JobDataMap;

            //string data = dataMap.GetString("value1"); 

            return Task.Run(() =>
            {
                _logger.LogInformation($"{DateTime.Now.ToString(CultureInfo.InvariantCulture)}：------MyJob2------");

            });
        }
    }
}