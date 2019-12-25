using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

using Quartz;
using Quartz.Impl.Matchers;
using Quartz.Spi;

using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.IO;
using SSS.Infrastructure.Util.Json;

using System;
using System.IO;
using System.Threading.Tasks;

namespace SSS.Application.Seedwork.Job
{
    /// <summary>
    /// JobStartup启动类
    /// </summary>

    [DIService(ServiceLifetime.Singleton, typeof(JobManager))]
    public class JobManager
    {
        private readonly ILogger<JobManager> _logger;
        private readonly ISchedulerFactory _schedulerFactory;
        private readonly IJobFactory _iocJobfactory;
        private IScheduler _scheduler;

        /// <summary>
        /// QuartzStartup
        /// </summary>
        /// <param name="iocJobfactory"></param>
        /// <param name="logger"></param>
        /// <param name="schedulerFactory"></param>
        public JobManager(IJobFactory iocJobfactory, ILogger<JobManager> logger, ISchedulerFactory schedulerFactory)
        {
            //1、声明一个调度工厂
            this._logger = logger;
            this._schedulerFactory = schedulerFactory;
            this._iocJobfactory = iocJobfactory;
        }

        /// <summary>
        /// 停止执行的job，并删除此job
        /// </summary>
        /// <param name="jobname"></param>
        /// <param name="job_group"></param>
        /// <returns></returns>
        public async Task<bool> UnscheduleJob(string jobname, string job_group = "job_group")
        {
            JobKey existKey = JobKey.Create(jobname, job_group);
            TriggerKey exisTriggerKey = new TriggerKey(jobname, job_group);
            await _scheduler.PauseJob(existKey);
            return await _scheduler.UnscheduleJob(exisTriggerKey) && await _scheduler.DeleteJob(existKey);
        }

        /// <summary>
        /// 开始
        /// </summary>
        /// <returns></returns>
        public async Task<string> Start()
        {
            //2、通过调度工厂获得调度器
            _scheduler = await _schedulerFactory.GetScheduler();
            _scheduler.JobFactory = this._iocJobfactory;//  替换默认工厂

            //3、开启调度器
            await _scheduler.Start();

            var json = IO.ReadAllText(Directory.GetCurrentDirectory() + "\\job.json");
            if (string.IsNullOrWhiteSpace(json))
            {
                _logger.LogError("------任务配置文件没有内容------");
                return await Task.FromResult("将触发器和任务器绑定到调度器中完成");
            }

            var jobject = Json.ToJObject(json);

            var result = jobject["JobConfig"];

            foreach (var item in result)
            {
                var jobclass = Type.GetType(item["JobClass"].ToString());  //类型
                var jobname = item["JobName"].ToString();   //任务名
                var jobgroup = item["JobGroup"].ToString();  //任务组
                var jobcron = item["JobCron"].ToString();   //Cron
                var value_result = item["JobValue"];//传值

                JobDataMap data = new JobDataMap();

                if (value_result != null)
                    foreach (var val in value_result)
                    {
                        var name = val["Name"].ToString();

                        switch (val["Type"].ToString())
                        {
                            case "String":
                                data.Add(name, val["Value"].ToString());
                                break;
                            case "Int":
                                data.Add(name, Convert.ToInt32(val["Value"]));
                                break;
                            case "Double":
                                data.Add(name, Convert.ToDouble(val["Value"]));
                                break;
                            case "Bool":
                                data.Add(name, Convert.ToBoolean(val["Value"]));
                                break;
                            default:
                                data.Add(name, val["Value"].ToJson());
                                break;
                        }
                    }

                //4、创建任务
                var jobDetail = JobBuilder.Create(jobclass).WithIdentity(jobname, jobgroup).UsingJobData(data).Build();

                //5、创建一个触发器
                ITrigger trigger = TriggerBuilder.Create().WithIdentity(jobname, jobgroup).WithCronSchedule(jobcron).ForJob(jobname, jobgroup).Build();

                //6、监听
                var jobListener = new JobListener { Name = jobname + "_listener" };
                IMatcher<JobKey> matcher = KeyMatcher<JobKey>.KeyEquals(jobDetail.Key);
                _scheduler.ListenerManager.AddJobListener(jobListener, matcher);

                //7、将触发器和任务器绑定到调度器中
                await _scheduler.ScheduleJob(jobDetail, trigger);
            }

            _logger.LogCritical("------任务调度开启------");

            return await Task.FromResult("将触发器和任务器绑定到调度器中完成");
        }

        /// <summary>
        /// 结束
        /// </summary>
        public void Stop()
        {
            if (_scheduler == null)
            {
                return;
            }

            if (_scheduler.Shutdown(waitForJobsToComplete: true).Wait(30000))
                _scheduler = null;
            else
            {
            }
            _logger.LogCritical("------任务调度关闭------");
        }
    }
}
