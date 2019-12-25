using Quartz;

using System.Threading;
using System.Threading.Tasks;
using Microsoft.Extensions.Logging;
using SSS.Infrastructure.Util.Log;

namespace SSS.Application.Seedwork.Job
{
    public class JobListener : IJobListener
    {
        public string Name { set; get; }

        /// <summary>
        /// Scheduler在JobDetail即将被执行，但又被TriggerListerner否决时会调用该方法
        /// </summary>
        /// <param name="context"></param>
        /// <param name="cancellationToken"></param>
        /// <returns></returns>
        public Task JobExecutionVetoed(IJobExecutionContext context, CancellationToken cancellationToken = default)
        {
            return Task.CompletedTask;
        }

        /// <summary>
        /// Scheduler在JobDetail将要被执行时调用这个方法。
        /// </summary>
        /// <param name="context"></param>
        /// <param name="cancellationToken"></param>
        /// <returns></returns>
        public Task JobToBeExecuted(IJobExecutionContext context, CancellationToken cancellationToken = default)
        {
            return Task.CompletedTask;
        }

        /// <summary>
        /// Scheduler在JobDetail被执行之后调用这个方法
        /// </summary>
        /// <param name="context"></param>
        /// <param name="jobException"></param>
        /// <param name="cancellationToken"></param>
        /// <returns></returns>
        public Task JobWasExecuted(IJobExecutionContext context, JobExecutionException jobException, CancellationToken cancellationToken = default)
        {
            var trigger = (Quartz.Impl.Triggers.SimpleTriggerImpl)context.Trigger;
            ApplicationLog.CreateLogger<JobListener>().LogInformation($" 监听------> 任务名：{trigger.JobKey}  运行次数：{trigger.TimesTriggered}  下次运行时间：{trigger.GetNextFireTimeUtc() }");
            return Task.CompletedTask;
        }
    }
}
