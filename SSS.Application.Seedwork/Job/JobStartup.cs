using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

using Quartz;
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

    [DIService(ServiceLifetime.Singleton, typeof(JobStartup))]
    public class JobStartup
    {

        private readonly ILogger<JobStartup> _logger;
        private readonly ISchedulerFactory _schedulerFactory;
        private readonly IJobFactory _iocJobfactory;
        private IScheduler _scheduler;

        /// <summary>
        /// QuartzStartup
        /// </summary>
        /// <param name="iocJobfactory"></param>
        /// <param name="logger"></param>
        /// <param name="schedulerFactory"></param>
        public JobStartup(IJobFactory iocJobfactory, ILogger<JobStartup> logger, ISchedulerFactory schedulerFactory)
        {
            //1、声明一个调度工厂
            this._logger = logger;
            this._schedulerFactory = schedulerFactory;
            this._iocJobfactory = iocJobfactory;
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
                var typestr = item["Type"].ToString();
                var type = Type.GetType(typestr);
                var name = item["Name"].ToString();
                var value = item["Value"].ToString();
                var group = item["Group"].ToString();

                //4、创建一个触发器
                var trigger = TriggerBuilder.Create().WithSimpleSchedule(x => x.WithIntervalInSeconds(2).RepeatForever()).Build();

                //5、创建任务
                var jobDetail = JobBuilder.Create(type).WithIdentity(name, group).UsingJobData("value1", value).Build();

                //6、将触发器和任务器绑定到调度器中
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
