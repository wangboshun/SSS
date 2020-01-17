using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

using Quartz;
using Quartz.Impl;
using Quartz.Impl.Triggers;

using SSS.Domain.System.Job.JobError;
using SSS.Domain.System.Job.JobInfo;
using SSS.Infrastructure.Repository.System.Job.JobError;
using SSS.Infrastructure.Repository.System.Job.JobInfo;
using SSS.Infrastructure.Util.DI;
using SSS.Infrastructure.Util.Json;
using SSS.Infrastructure.Util.Log;

using System;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Application.Job.JobSetting.Listener
{
    public class JobListener : IJobListener
    {
        private static readonly object _lock = new object();

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
            try
            {
                lock (_lock)
                {var trigger = (CronTriggerImpl)((JobExecutionContextImpl)context).Trigger;
                    var result = context.Scheduler.Context.Get(trigger.FullName + "_Result"); /*返回结果*/
                    var exception = context.Scheduler.Context.Get(trigger.FullName + "_Exception"); /*异常信息*/
                    var time = context.Scheduler.Context.Get(trigger.FullName + "_Time"); /* 耗时*/
                     
                    var jobinfo_repository = IocEx.Instance.GetService<IJobInfoRepository>();

                    var job = jobinfo_repository.Get(x => x.JobName.Equals(trigger.Name) && x.JobGroup.Equals(trigger.JobGroup) && x.IsDelete == 0);

                    if (job == null)
                    {
                        job = new JobInfo
                        {
                            Id = Guid.NewGuid().ToString(),
                            JobName = trigger.JobName,
                            JobGroup = trigger.JobGroup,
                            JobValue = context.JobDetail.JobDataMap.ToJson(),
                            IsDelete = 0,
                            JobCount = 1,
                            JobClass = context.JobDetail.JobType.FullName,
                            JobStatus = (int)TriggerState.Normal,
                            JobResult = result.ToJson(),
                            JobCron = trigger.CronExpressionString,
                            CreateTime = DateTime.Now,
                            JobRunTime = time != null ? Convert.ToInt32(time) : 0,
                            JobStartTime = trigger.StartTimeUtc.LocalDateTime,
                            JobNextTime = trigger.GetNextFireTimeUtc().GetValueOrDefault().LocalDateTime
                        };
                        jobinfo_repository.Add(job, true);
                    }
                    else
                    {
                        job.JobStartTime = trigger.StartTimeUtc.LocalDateTime;
                        job.JobValue = context.JobDetail.JobDataMap.ToJson();
                        job.JobCron = trigger.CronExpressionString;
                        job.JobCount += 1;
                        job.JobRunTime = time != null ? Convert.ToInt32(time) : 0;
                        job.JobNextTime = trigger.GetNextFireTimeUtc().GetValueOrDefault().LocalDateTime;
                        job.UpdateTime = DateTime.Now;
                        job.JobResult = result.ToJson();
                        jobinfo_repository.Update(job, true);
                    }

                    //记录错误
                    if (exception != null)
                    {
                        var joberror_repository = IocEx.Instance.GetService<IJobErrorRepository>();
                        var error = new JobError
                        {
                            Id = Guid.NewGuid().ToString(),
                            JobId = job.Id,
                            JobCount = job.JobCount,
                            IsDelete = 0,
                            CreateTime = DateTime.Now,
                            Message = exception.ToJson()
                        };

                        joberror_repository.Add(error, true);
                    }

                    ApplicationLog.CreateLogger<JobListener>().LogInformation($"任务监听：{job.ToJson()}");
                    return Task.CompletedTask;
                }
            }
            catch (Exception ex)
            {
                ApplicationLog.CreateLogger<JobListener>().LogError(new EventId(ex.HResult), ex, "---JobWasExecuted Exception---");
                return Task.CompletedTask;
            }
        }
    }
}