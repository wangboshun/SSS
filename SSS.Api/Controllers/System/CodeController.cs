using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Mvc;

using Quartz;

using SSS.Api.Bootstrap;
using SSS.Api.Seedwork.Controller;
using SSS.Application.System;

using System.IO;
using System.Net;
using System.Runtime.InteropServices;

namespace SSS.Api.Controllers.System
{
    /// <summary>	
    /// 代码生成	
    /// </summary>	
    [ApiVersion("3.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [ApiController]
    public class CodeController : ApiBaseController
    {
        private static string current_path;
        private readonly IWebHostEnvironment _env;
        private readonly IGeneratorCodeService _generatorCodeService;

        private readonly ISchedulerFactory _schedulerFactory;
        private IScheduler _scheduler;

        /// <summary>
        /// CodeController
        /// </summary>
        /// <param name="env"></param>
        /// <param name="generatorCodeService"></param>
        public CodeController(IWebHostEnvironment env, IGeneratorCodeService generatorCodeService, ISchedulerFactory schedulerFactory)
        {
            this._schedulerFactory = schedulerFactory;
            _env = env;
            current_path = _env.ContentRootPath;
            _generatorCodeService = generatorCodeService;
        }

        /// <summary>	
        /// 主页	
        /// </summary>	
        /// <returns></returns>	
        [HttpGet("index")]
        public ContentResult Index()
        {
            //1、通过调度工厂获得调度器
            _scheduler = _schedulerFactory.GetScheduler().Result;
            //2、开启调度器
            _scheduler.Start();
            //3、创建一个触发器
            var trigger = TriggerBuilder.Create()
                .WithSimpleSchedule(x => x.WithIntervalInSeconds(5).RepeatForever())//每两秒执行一次
                .Build();
            //4、创建任务
            var jobDetail = JobBuilder.Create<MyJob>()
                .WithIdentity("job", "group")
                .Build();
            //5、将触发器和任务器绑定到调度器中
            _scheduler.ScheduleJob(jobDetail, trigger);

            string html = "";
            string filepath = RuntimeInformation.IsOSPlatform(OSPlatform.Linux)
                ? current_path + "//codegenerator.html"
                : current_path + "\\codegenerator.html";

            using (StreamReader sr = new StreamReader(filepath))
            {
                html = sr.ReadToEnd();
            }

            return new ContentResult
            {
                ContentType = "text/html",
                StatusCode = (int)HttpStatusCode.OK,
                Content = html
            };
        }

        /// <summary>	
        /// 生成操作	
        /// </summary>	
        /// <returns></returns>	
        [HttpPost("createcode")]
        public IActionResult CreateCode()
        {
            var result = _generatorCodeService.CreateCode(HttpContext.Request.Form["class_name"],
                HttpContext.Request.Form["namespace_name"], HttpContext.Request.Form["fields"]);
            return ApiResponse(result);
        }
    }
}