using Hangfire;
using Hangfire.MySql.Core;
using MediatR;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Controllers;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.FileProviders;
using Microsoft.Extensions.Options;
using Senparc.CO2NET;
using Senparc.CO2NET.RegisterServices;
using Senparc.Weixin;
using Senparc.Weixin.Entities;
using Senparc.Weixin.RegisterServices;
using Senparc.Weixin.WxOpen;
using SSS.Api.Bootstrap;
using SSS.Api.Middware;
using SSS.Api.Seedwork;
using SSS.Api.Seedwork.Filter;
using System.IO;
using System.Reflection;

namespace SSS.Api
{
    /// <summary>
    /// Startup
    /// </summary>
    public class Startup
    {
        /// <summary>
        /// Startup
        /// </summary>
        /// <param name="configuration">IConfiguration</param>
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        /// <summary>
        /// Configuration
        /// </summary>
        public IConfiguration Configuration { get; }

        /// <summary>
        /// ConfigureServices
        /// </summary>
        /// <param name="services">IServiceCollection</param>
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddMvc(options =>
            {
                //全局Action Exception Result过滤器
                options.Filters.Add<MvcFilter>();
            }).SetCompatibilityVersion(CompatibilityVersion.Version_2_2);

            services.AddMemoryCacheEx();

            services.AddSingleton(typeof(IControllerActivator), typeof(SSS.Api.Seedwork.Controller.BaseControllerActivator));

            services.AddSingleton<IHttpContextAccessor, HttpContextAccessor>();

            //设置授权 Api
            services.AddAuthorization();

            //设置认证 Api
            services.AddAuthentication("Bearer")
                .AddJwtBearer("Bearer", options =>
                {
                    options.Authority = "http://localhost:456";
                    options.RequireHttpsMetadata = false;
                    options.SaveToken = true;
                    options.Audience = "api_test1";
                });

            //AutoMapper映射
            services.AddAutoMapperSupport();

            //MediatR
            services.AddMediatR(typeof(Startup));

            //集中注入
            services.AddService();

            //Redis
            //services.AddRedisCache(Configuration.GetSection("Redis"));    //方式一
            //services.AddRedisCache();                                     //方式二
            //services.AddRedisCache(options =>                          //方式三
            //{
            //    options.host = "192.168.1.148";
            //    options.port = 6379;
            //});

            //MemCache
            //services.AddMemCached(Configuration.GetSection("MemCache"));
            //services.AddMemCached();

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
                options.SqlFormatter = new StackExchange.Profiling.SqlFormatters.InlineFormatter();

                // (Optional) You can disable "Connection Open()", "Connection Close()" (and async variant) tracking.
                // (defaults to true, and connection opening/closing is tracked)
                options.TrackConnectionOpenClose = true;
            }).AddEntityFramework();

            services.AddHangfire(config =>
            {
                config.UseStorage(new MySqlStorage(Configuration.GetConnectionString("MYSQLConnection")));
                //config.UseSqlServerStorage(Configuration.GetConnectionString("MSSQLConnection"));
            });

            services.AddHangfireServer();

            services.AddSenparcGlobalServices(Configuration)//Senparc.CO2NET 全局注册
                .AddSenparcWeixinServices(Configuration);//Senparc.Weixin 注册 

        }
        /// <summary>
        /// Configure
        /// </summary>
        /// <param name="app">IApplicationBuilder</param>
        /// <param name="env">IHostingEnvironment</param>
        public void Configure(IApplicationBuilder app, IHostingEnvironment env, IOptions<SenparcSetting> senparcSetting, IOptions<SenparcWeixinSetting> senparcWeixinSetting)
        {
            IRegisterService register = RegisterService.Start(env, senparcSetting.Value).UseSenparcGlobal();

            if (env.IsDevelopment())
                app.UseDeveloperExceptionPage();
            else
                app.UseHsts();

            //异常拦截
            app.UseApiException();

            GlobalJobFilters.Filters.Add(new AutomaticRetryAttribute() { Attempts = 1 });
            app.UseHangfireServer();
            app.UseHangfireDashboard("/hangfire", new DashboardOptions()
            {
                Authorization = new[] { new CustomAuthorizeFilter() }

            });

            ////认证中间件
            app.UseAuthentication();
             
            //拦截Urls
            app.UseMiddleware<UrlsMiddleware>();

            //http上下文
            app.UseHttpContext();

            app.UseMiniProfiler();

            ////RedisCahce
            //app.UseRedisCache(options =>
            //{
            //    options.host = Configuration.GetSection("Redis:host").Value;
            //    options.port = Convert.ToInt32(Configuration.GetSection("Redis:port").Value);
            //});

            //Swagger
            app.UseSwagger();
            app.UseSwaggerUI(options =>
            {
                options.RoutePrefix = "docs";
                options.DocumentTitle = "SSS Project";
                options.SwaggerEndpoint("/swagger/v1/swagger.json", "SSS API V1");
                options.IndexStream = () => GetType().GetTypeInfo().Assembly.GetManifestResourceStream("SSS.Api.miniprofiler.html");
            });
            app.UseCors(builder => builder.AllowAnyHeader().AllowAnyOrigin().AllowAnyMethod());

            app.UseHttpsRedirection();

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

            app.UseMvc();

            register.UseSenparcWeixin(senparcWeixinSetting.Value, senparcSetting.Value).RegisterWxOpenAccount(senparcWeixinSetting.Value, "SSS");

        }
    }
}
