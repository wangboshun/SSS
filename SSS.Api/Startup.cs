using FluentValidation.AspNetCore;

using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.FileProviders;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Options;

using Quartz;
using Quartz.Impl;

using Senparc.CO2NET;
using Senparc.CO2NET.RegisterServices;
using Senparc.Weixin;
using Senparc.Weixin.Entities;
using Senparc.Weixin.RegisterServices;
using Senparc.Weixin.WxOpen;

using SSS.Api.Bootstrap;
using SSS.Api.Seedwork.Filter;
using SSS.Api.Seedwork.Json;
using SSS.Api.Seedwork.Middleware;
using SSS.Application.Job;

using StackExchange.Profiling.SqlFormatters;

using System.IO;
using System.Linq;
using System.Reflection;

namespace SSS.Api
{
    /// <summary>
    ///     Startup
    /// </summary>
    public class Startup
    {
        /// <summary>
        ///     Startup
        /// </summary>
        /// <param name="configuration">IConfiguration</param>
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        /// <summary>
        ///     Configuration
        /// </summary>
        public IConfiguration Configuration { get; }

        /// <summary>
        ///     ConfigureServices
        /// </summary>
        /// <param name="services">IServiceCollection</param>
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddMvc(options =>
                {
                    //全局Action Exception Result过滤器
                    options.Filters.Add<MvcFilter>();
                })
                //.AddNewtonsoftJson(options =>
                //{
                //    options.SerializerSettings.DateFormatString = "yyyy-MM-dd HH:mm:ss";
                //})
                .AddFluentValidation(config =>
                {
                    config.RunDefaultMvcValidationAfterFluentValidationExecutes = false;
                })
                .ConfigureApiBehaviorOptions(config =>
                {
                    //关闭默认模型验证过滤器
                    config.SuppressModelStateInvalidFilter = true;
                })
                .SetCompatibilityVersion(CompatibilityVersion.Version_3_0)
                .AddJsonOptions(options =>
                {
                    options.JsonSerializerOptions.Converters.Add(new DateTimeConverter());
                    options.JsonSerializerOptions.Converters.Add(new DateTimeNullConverter());
                });

            services.AddMemoryCacheEx();

            //services.AddSingleton<ITypeActivatorCache, DefaultTypeActivatorCache>();

            ////url https://blog.csdn.net/u013710468/article/details/83588725
            //var defaultActivator = services.FirstOrDefault(c => c.ServiceType == typeof(IControllerActivator));
            //if (defaultActivator != null)
            //{
            //    services.Remove(defaultActivator);
            //    services.AddSingleton<IControllerActivator, BaseControllerActivator>();
            //}

            //AutoMapper映射
            services.AddAutoMapperSupport();

            //集中注入
            services.AddService();

            //MemoryCache
            services.AddMemoryCache();

            //Swagger
            services.AddSwagger();

            //ApiVersion
            services.AddApiVersion();

            services.AddMiniProfiler(options =>
            {
                // (Optional) Path to use for profiler URLs, default is /mini-profiler-resources
                options.RouteBasePath = "/profiler";

                // (Optional) Control which SQL formatter to use, InlineFormatter is the default
                options.SqlFormatter = new InlineFormatter();

                // (Optional) You can disable "Connection Open()", "Connection Close()" (and async variant) tracking.
                // (defaults to true, and connection opening/closing is tracked)
                options.TrackConnectionOpenClose = true;
            }).AddEntityFramework();

            services.AddSenparcGlobalServices(Configuration) //Senparc.CO2NET 全局注册
                .AddSenparcWeixinServices(Configuration); //Senparc.Weixin 注册   

            //注入 Quartz调度类 
            services.AddSingleton<ISchedulerFactory, StdSchedulerFactory>();//注册ISchedulerFactory的实例。

            services.AddControllers();
        }

        /// <summary>
        /// Configure
        /// </summary>
        /// <param name="app"></param>
        /// <param name="_httpContextFactory"></param>
        /// <param name="env"></param>
        /// <param name="senparcSetting"></param>
        /// <param name="senparcWeixinSetting"></param>
        /// <param name="appLifetime"></param>
        public void Configure(IApplicationBuilder app, IHttpContextFactory _httpContextFactory, IWebHostEnvironment env, IOptions<SenparcSetting> senparcSetting, IOptions<SenparcWeixinSetting> senparcWeixinSetting, IHostApplicationLifetime appLifetime)
        {
            IRegisterService register = RegisterService.Start(env, senparcSetting.Value).UseSenparcGlobal();

            if (env.IsDevelopment())
                app.UseDeveloperExceptionPage();
            else
                app.UseHsts();

            //跨域
            app.UseHttpsRedirection().UseCors(options =>
            {
                options.AllowAnyOrigin();
                options.AllowAnyMethod();
                options.AllowAnyHeader();
            });

            //跨域
            app.UseCors(options =>
            {
                options.AllowAnyHeader();
                options.AllowAnyMethod();
                options.AllowAnyOrigin();
                //options.AllowCredentials();
            });

            //异常拦截
            app.UseApiException();

            ////认证中间件
            app.UseAuthentication();

            //Url重定向
            app.UseMiddleware<UrlsMiddleware>();

            //拦截登录
            //app.UseMiddleware<LoginMiddleware>();

            //http上下文
            app.UseHttpContext();

            //MiniProfiler
            app.UseMiniProfiler();

            //Swagger
            Swagger(app);

            //跨域
            app.UseCors(builder => builder.AllowAnyHeader().AllowAnyOrigin().AllowAnyMethod());

            //Https
            app.UseHttpsRedirection();

            //自定义文件浏览
            UseDefaultStaticFile(app);

            //路由
            app.UseRouting();

            //执行路由
            app.UseEndpoints(endpoints => { endpoints.MapControllers(); });

            //公众号注入
            register.UseSenparcWeixin(senparcWeixinSetting.Value, senparcSetting.Value).RegisterWxOpenAccount(senparcWeixinSetting.Value, "SSS");

            var job_service = app.ApplicationServices.GetRequiredService<IJobManager>();


            appLifetime.ApplicationStarted.Register(() =>
            {
                job_service.Start().Wait();
                //网站启动完成执行
            });

            appLifetime.ApplicationStopped.Register(() =>
            {
                job_service.Stop();
                //网站停止完成执行
            });
        }

        /// <summary>
        ///     Swagger
        /// </summary>
        /// <param name="app"></param>
        private void Swagger(IApplicationBuilder app)
        {
            app.UseSwagger();
            app.UseSwaggerUI(options =>
            {
                //遍历版本号
                typeof(ApiVersions).GetEnumNames().OrderByDescending(e => e).ToList().ForEach(version =>
                {
                    options.SwaggerEndpoint($"/swagger/{version}/swagger.json", $"{GetVersion(version)}");
                });

                options.RoutePrefix = "docs";
                options.DocumentTitle = "SSS Project";

                options.IndexStream = () => GetType().GetTypeInfo().Assembly.GetManifestResourceStream("SSS.Api.miniprofiler.html");
            });
        }

        private static string GetVersion(string version)
        {
            switch (version)
            {
                case "v1":
                    return "交易接口";
                case "v2":
                    return "权限接口";
                case "v3":
                    return "系统接口";
                default:
                    return version;
            }
        }

        /// <summary>
        ///     文件浏览
        /// </summary>
        /// <param name="app"></param>
        private void UseDefaultStaticFile(IApplicationBuilder app)
        {
            string contentRoot = Directory.GetCurrentDirectory();
            IFileProvider fileProvider = new PhysicalFileProvider(
                Path.Combine(contentRoot, "File"));

            app.UseStaticFiles(new StaticFileOptions
            {
                FileProvider = fileProvider,
                RequestPath = "/file"
            });

            app.UseDirectoryBrowser();

            app.UseDirectoryBrowser(new DirectoryBrowserOptions
            {
                FileProvider = fileProvider,
                RequestPath = "/file"
            });
        }
    }
}