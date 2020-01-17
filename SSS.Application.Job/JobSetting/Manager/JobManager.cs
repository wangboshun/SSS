using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

using Newtonsoft.Json.Linq;

using Quartz;
using Quartz.Impl.Matchers;
using Quartz.Impl.Triggers;
using Quartz.Spi;

using SSS.Application.Job.JobSetting.Listener;
using SSS.Domain.System.Job.JobInfo;
using SSS.Infrastructure.Repository.System.Job.JobInfo;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.DI;
using SSS.Infrastructure.Util.IO;
using SSS.Infrastructure.Util.Json;

using System;
using System.IO;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;

namespace SSS.Application.Job.JobSetting.Manager
{
    /// <summary>
    /// JobStartup启动类
    /// </summary>
    [DIService(ServiceLifetime.Singleton, typeof(IJobManager))]
    public class JobManager : IJobManager
    {
        private readonly IJobFactory _jobFactory;
        private readonly ILogger<JobManager> _logger;
        private readonly ISchedulerFactory _schedulerFactory;
        private IScheduler _scheduler;

        /// <summary>
        /// JobStartup
        /// </summary>
        /// <param name="jobFactory"></param>
        /// <param name="logger"></param>
        /// <param name="schedulerFactory"></param>
        public JobManager(IJobFactory jobFactory, ILogger<JobManager> logger, ISchedulerFactory schedulerFactory)
        {
            //1、声明一个调度工厂
            _logger = logger;
            _schedulerFactory = schedulerFactory;
            _jobFactory = jobFactory;

            Init();
        }

        #region Job CURD

        /// <summary>
        /// 添加job
        /// </summary>
        /// <param name="job_name"></param>
        /// <param name="job_group"></param>
        /// <returns></returns>
        public async Task<bool> AddJob(string job_name, string job_group, string job_cron, string job_value, string job_class_str)
        {
            var data = GetJobDataMapByStr(job_value);
            var jobclass = Type.GetType(job_class_str);

            //4、创建任务
            var jobDetail = JobBuilder.Create(jobclass).WithIdentity(job_name, job_group).UsingJobData(data).Build();

            //5.1 构建器
            var builder = CronScheduleBuilder.CronSchedule(job_cron);

            //5.2 触发器
            var trigger_builder = TriggerBuilder.Create();

            //6、构建
            var trigger = trigger_builder.WithIdentity(job_name, job_group)
                .ForJob(job_name, job_group)
                .WithSchedule(builder.WithMisfireHandlingInstructionFireAndProceed())
                .Build();

            //6、监听
            AddListener(jobDetail);

            //7、将触发器和任务器绑定到调度器中
            await _scheduler.ScheduleJob(jobDetail, trigger);

            return GeTriggerState(job_name, job_group) == TriggerState.Normal;
        }

        /// <summary>
        /// 删除job
        /// </summary>
        /// <param name="job_name"></param>
        /// <param name="job_group"></param>
        /// <returns></returns>
        public async Task<bool> DeleteJob(string job_name, string job_group)
        {
            var key = GetJobKey(job_name, job_group);
            //TriggerKey trigger = GetTriggerKey(job_name, job_group);
            //await _scheduler.PauseJob(key);
            //await _scheduler.UnscheduleJob(trigger);
            //_scheduler.ListenerManager.RemoveJobListener(job_name + "_listener");
            return await _scheduler.DeleteJob(key);
        }

        /// <summary>
        /// 查看Job信息
        /// </summary>
        /// <param name="job_name"></param>
        /// <param name="job_group"></param>
        /// <returns></returns>
        public IJobDetail GetJobDetail(string job_name, string job_group)
        {
            var key = GetJobKey(job_name, job_group);
            return _scheduler.GetJobDetail(key).Result;
        }

        /// <summary>
        /// 获取Trigger状态
        /// </summary>
        /// <param name="job_name"></param>
        /// <param name="job_group"></param>
        /// <returns></returns>
        public TriggerState GeTriggerState(string job_name, string job_group)
        {
            var key = GetTriggerKey(job_name, job_group);
            return _scheduler.GetTriggerState(key).Result;
        }

        /// <summary>
        /// 查看Trigger信息
        /// </summary>
        /// <param name="job_name"></param>
        /// <param name="job_group"></param>
        /// <returns></returns>
        public ITrigger GetTrigger(string job_name, string job_group)
        {
            var key = GetTriggerKey(job_name, job_group);
            return _scheduler.GetTrigger(key).Result;
        }

        /// <summary>
        /// 暂停Job
        /// </summary>
        public async Task PauseJob(string job_name, string job_group)
        {
            var key = GetJobKey(job_name, job_group);
            var trigger = GetTriggerKey(job_name, job_group);
            var status = GetTriggerState(trigger);
            if (status == TriggerState.Paused)
                return;
            await _scheduler.PauseJob(key);
            await _scheduler.PauseTrigger(trigger);
        }

        /// <summary>
        /// 恢复Job
        /// </summary>
        public async Task ResumeJob(string job_name, string job_group)
        {
            var key = GetJobKey(job_name, job_group);
            var trigger = GetTriggerKey(job_name, job_group);
            var status = GetTriggerState(trigger);
            if (status == TriggerState.Normal)
                return;
            await _scheduler.ResumeJob(key);
            await _scheduler.ResumeTrigger(trigger);
        }

        /// <summary>
        /// 修改Job
        /// </summary>
        public async Task UpdateJob(string job_name, string job_group, string job_cron)
        {
            var key = GetTriggerKey(job_name, job_group);
            ICronTrigger cron = new CronTriggerImpl(job_name, job_group, job_name, job_group, job_cron);
            await _scheduler.RescheduleJob(key, cron);
        }

        #endregion Job CURD

        #region 私有方法，特殊处理

        /// <summary>
        /// 添加Job监听
        /// </summary>
        /// <param name="detail"></param>
        private void AddListener(IJobDetail detail)
        {
            var jobListener = new JobListener { Name = detail + "_listener" };
            IMatcher<JobKey> matcher = KeyMatcher<JobKey>.KeyEquals(detail.Key);
            _scheduler.ListenerManager.AddJobListener(jobListener, matcher);
        }

        /// <summary>
        /// 获取参数,根据JToken（json文件的内容）
        /// </summary>
        /// <param name="result"></param>
        /// <returns></returns>
        private JobDataMap GetJobDataMap(JToken result)
        {
            var data = new JobDataMap();

            if (result == null) return data;

            foreach (var val in result)
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

            return data;
        }

        /// <summary>
        /// 获取参数,根据字符串（数据库存储的内容）
        /// </summary>
        /// <param name="str"></param>
        /// <returns></returns>
        private JobDataMap GetJobDataMapByStr(string str)
        {
            var result = JObject.Parse(str);

            var data = new JobDataMap();

            if (result != null)
                foreach (var val in result.Children())
                {
                    var v = ((JValue)((JProperty)val).Value).Value;
                    var t = ((JValue)((JProperty)val).Value).Type.ToString();
                    switch (t)
                    {
                        case "String":
                            data.Add(val.Path, v.ToString());
                            break;

                        case "Int":
                            data.Add(val.Path, Convert.ToInt32(v));
                            break;

                        case "Double":
                            data.Add(val.Path, Convert.ToDouble(v));
                            break;

                        case "Bool":
                            data.Add(val.Path, Convert.ToBoolean(v));
                            break;

                        default:
                            data.Add(val.Path, v.ToString());
                            break;
                    }
                }

            return data;
        }

        /// <summary>
        /// 获取JobKey
        /// </summary>
        /// <param name="job_name"></param>
        /// <param name="job_group"></param>
        /// <returns></returns>
        private JobKey GetJobKey(string job_name, string job_group)
        {
            return JobKey.Create(job_name, job_group);
        }

        /// <summary>
        /// 获取TriggerKey
        /// </summary>
        /// <param name="job_name"></param>
        /// <param name="job_group"></param>
        /// <returns></returns>
        private TriggerKey GetTriggerKey(string job_name, string job_group)
        {
            return new TriggerKey(job_name, job_group);
        }

        /// <summary>
        /// 获取触发器状态
        /// </summary>
        /// <param name="key"></param>
        /// <returns></returns>
        private TriggerState GetTriggerState(TriggerKey key)
        {
            return _scheduler.GetTriggerState(key).Result;
        }

        #endregion 私有方法，特殊处理

        #region 启动、关闭

        /// <summary>
        /// 开始
        /// </summary>
        /// <returns></returns>
        public async Task<string> Start()
        {
            try
            {
                var file = RuntimeInformation.IsOSPlatform(OSPlatform.Linux)
                    ? Directory.GetCurrentDirectory() + "//job.json"
                    : Directory.GetCurrentDirectory() + "\\job.json";

                Encoding.RegisterProvider(CodePagesEncodingProvider.Instance);
                var json = IO.ReadAllText(file, Encoding.GetEncoding("GB2312"));
                if (string.IsNullOrWhiteSpace(json))
                {
                    _logger.LogError("------JobManager 任务配置文件没有内容------");
                    return await Task.FromResult("JobManager 任务配置文件没有内容");
                }

                var jobject = json.ToJObject();
                if (jobject == null)
                {
                    _logger.LogError("------JobManager Json格式错误------");
                    return await Task.FromResult("JobManager Json格式错误");
                }

                var result = jobject["JobConfig"];

                var jobinfo_repository = IocEx.Instance.GetService<IJobInfoRepository>();

                foreach (var item in result)
                {
                    var type_str = item["JobClass"].ToString(); //类型字符串
                    var job_name = item["JobName"].ToString(); //任务名
                    var job_group = item["JobGroup"].ToString(); //任务组
                    var job_cron = item["JobCron"].ToString(); //Cron
                    var value_result = item["JobValue"]; //传值
                    var job_status = item["JobStatus"]; //状态

                    if (string.IsNullOrWhiteSpace(job_name) ||
                        string.IsNullOrWhiteSpace(job_cron) ||
                        string.IsNullOrWhiteSpace(type_str) ||
                        string.IsNullOrWhiteSpace(job_group))
                        continue;

                    var job = jobinfo_repository.Get(x => x.JobName.Equals(job_name) && x.JobGroup.Equals(job_group));

                    //如果是删除状态
                    if (job != null && job.JobStatus == (int)TriggerState.None)
                        continue;

                    var job_class = Type.GetType(type_str); //类型
                    if (job_class == null)
                        continue;

                    var data = GetJobDataMap(value_result);

                    //4、创建任务
                    var job_detail = JobBuilder.Create(job_class).WithIdentity(job_name, job_group).UsingJobData(data).Build();

                    //5.1 构建器
                    var builder = CronScheduleBuilder.CronSchedule(job_cron);

                    //5.2 触发器
                    var trigger_builder = TriggerBuilder.Create();

                    //6、构建
                    var trigger = trigger_builder.WithIdentity(job_name, job_group)
                        .ForJob(job_name, job_group)
                        .WithSchedule(builder.WithMisfireHandlingInstructionFireAndProceed())
                        .Build();

                    //7、监听
                    AddListener(job_detail);

                    //8、将触发器和任务器绑定到调度器中
                    await _scheduler.ScheduleJob(job_detail, trigger);

                    //9、如果是暂停状态
                    if (job != null && job.JobStatus == (int)TriggerState.Paused || !job_status.Value<bool>())
                        await PauseJob(job_name, job_group);
                }

                _logger.LogCritical("------任务调度开启------");

                return await Task.FromResult("将触发器和任务器绑定到调度器中完成");
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---JobManager Start Exception---");
                return await Task.FromResult("任务调度器启动异常");
            }
        }

        /// <summary>
        /// 结束
        /// </summary>
        public void Stop()
        {
            if (_scheduler == null) return;

            if (_scheduler.Shutdown(true).Wait(30000))
                _scheduler = null;

            _logger.LogCritical("------任务调度关闭------");
        }

        /// <summary>
        /// 初始化
        /// </summary>
        /// <returns></returns>
        private async Task Init()
        {
            //2、通过调度工厂获得调度器
            _scheduler = await _schedulerFactory.GetScheduler();
            _scheduler.JobFactory = _jobFactory; //  替换默认工厂

            //3、开启调度器
            await _scheduler.Start();
        }

        #endregion 启动、关闭
    }
}