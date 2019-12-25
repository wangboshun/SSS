using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

using Quartz;

using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Json;

using System;
using System.Text;
using System.Threading.Tasks;

namespace SSS.Application.Seedwork.Job
{
    /// <summary>
    /// MyJob1
    /// </summary>
    [DIService(ServiceLifetime.Transient, typeof(MyJob1))]
    public class MyJob1 : IJob
    {
        private readonly ILogger<MyJob1> _logger;

        /// <summary>
        /// MyJob
        /// </summary>
        /// <param name="logger"></param>
        public MyJob1(ILogger<MyJob1> logger)
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
            StringBuilder str = new StringBuilder();

            JobKey key = context.JobDetail.Key;
            JobDataMap dataMap = context.JobDetail.JobDataMap;
            str.Append($"正在运行---> 任务：{key} ");

            if (dataMap.Count > 0)
                str.Append($"任务传递参数：{ dataMap.ToJson() } ");

            str.Append("任务时间：" + DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss:ffffff "));

            return Task.Run(() =>
            {
                _logger.LogDebug(str.ToString());

            });
        }
    }
}