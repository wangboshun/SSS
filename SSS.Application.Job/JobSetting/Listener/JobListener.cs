using Microsoft.Extensions.Logging;

using Quartz;

using SqlSugar;

using SSS.Domain.System.Job.JobError;
using SSS.Domain.System.Job.JobInfo;
using SSS.Infrastructure.Util.Config;
using SSS.Infrastructure.Util.Json;
using SSS.Infrastructure.Util.Log;

using System;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Application.Job.JobSetting.Listener
{
    public class JobListener : IJobListener
    {
        private static object _lock = new object();

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
            lock (_lock)
            {
                SqlSugarClient db = new SqlSugarClient(
                    new ConnectionConfig()
                    {
                        ConnectionString = JsonConfig.GetSectionValue("ConnectionStrings:MYSQLConnection"),
                        DbType = DbType.MySql,
                        IsAutoCloseConnection = true
                    });

                var trigger = (Quartz.Impl.Triggers.CronTriggerImpl)((Quartz.Impl.JobExecutionContextImpl)context).Trigger;

                var result = context.Scheduler.Context.Get(trigger.FullName + "_Result"); /*返回结果*/
                var exception = context.Scheduler.Context.Get(trigger.FullName + "_Exception");  /*异常信息*/
                var time = context.Scheduler.Context.Get(trigger.FullName + "_Time"); /* 耗时*/

                var job = db.Queryable<JobInfo>().Where(x => x.JobName.Equals(trigger.Name) && x.JobGroup.Equals(trigger.JobGroup) && x.IsDelete == 0)?.First();

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
                    db.Insertable(job).ExecuteCommand();
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
                    db.Updateable(job).ExecuteCommand();
                }

                //记录错误
                if (exception != null)
                {
                    var error = new JobError
                    {
                        Id = Guid.NewGuid().ToString(),
                        JobId = job.Id,
                        JobCount = job.JobCount,
                        IsDelete = 0,
                        CreateTime = DateTime.Now,
                        Message = exception.ToJson()
                    };
                    db.Insertable(error).ExecuteCommand();
                }

                ApplicationLog.CreateLogger<JobListener>().LogInformation($"任务监听：{job.ToJson()}");
                return Task.CompletedTask;
            }
        }
    }
}
