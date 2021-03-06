﻿using Autofac;

using FluentValidation.AspNetCore;

using HealthChecks.UI.Client;

using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Diagnostics.HealthChecks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.FileProviders;
using Microsoft.Extensions.Hosting;
using Microsoft.IdentityModel.Tokens;

using Quartz;
using Quartz.Impl;

using SSS.Api.Bootstrap;
using SSS.Api.HealthCheck;
using SSS.Api.Seedwork.Filter;
using SSS.Api.Seedwork.Json;
using SSS.Api.Seedwork.Middleware;
using SSS.Application.Job.JobSetting.Manager;
using SSS.Infrastructure.Util.Config;
using SSS.Infrastructure.Util.DI;
using SSS.Infrastructure.Util.Enum;

using StackExchange.Profiling.SqlFormatters;

using System;
using System.IO;
using System.Reflection;
using System.Text;

namespace SSS.Api
{
    /// <summary>
    /// Startup
    /// </summary>
    public class Startup
    {
        /// <summary>
        /// Configuration
        /// </summary>
        public IConfiguration Configuration { get; }

        /// <summary>
        /// Startup
        /// </summary>
        /// <param name="configuration">IConfiguration</param>
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        /// <summary>
        /// ConfigureContainer
        /// </summary>
        /// <param name="builder"></param>
        public void ConfigureContainer(ContainerBuilder builder)
        {
            //builder.AddAutoFacService();
        }

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

                // (Optional) You can disable "Connection Open()", "Connection Close()" (and async
                // variant) tracking. (defaults to true, and connection opening/closing is tracked)
                options.TrackConnectionOpenClose = true;
            }).AddEntityFramework();

            //注入 Quartz调度类
            services.AddSingleton<ISchedulerFactory, StdSchedulerFactory>(); //注册ISchedulerFactory的实例。

            services.AddHealthChecksUI().AddHealthChecks().AddCheck<RandomHealthCheck>("random");

            //添加jwt验证：
            services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
                .AddJwtBearer(options =>
                {
                    options.TokenValidationParameters = new TokenValidationParameters
                    {
                        ValidateIssuer = true,//是否验证Issuer
                        ValidateAudience = true,//是否验证Audience
                        ValidateLifetime = true,//是否验证失效时间
                        ClockSkew = TimeSpan.FromSeconds(30),
                        ValidateIssuerSigningKey = true,//是否验证SecurityKey
                        ValidAudience = JsonConfig.GetSectionValue("Auth:Domain"),//Audience
                        ValidIssuer = JsonConfig.GetSectionValue("Auth:Domain"),//Issuer，这两项和前面签发jwt的设置一致
                        IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(JsonConfig.GetSectionValue("Auth:SecurityKey")))//拿到SecurityKey
                    };
                });

            services.AddControllers();
        }

        /// <summary>
        /// Configure
        /// </summary>
        /// <param name="app"></param>
        /// <param name="_httpContextFactory"></param>
        /// <param name="env"></param>
        /// <param name="appLifetime"></param>
        public void Configure(IApplicationBuilder app, IHttpContextFactory _httpContextFactory, IHostEnvironment env, IHostApplicationLifetime appLifetime)
        {
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

            //Url重定向
            app.UseMiddleware<UrlsMiddleware>();

            //拦截登录
            //app.UseMiddleware<LoginMiddleware>();

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

            //开启认证 必须放在Routing和Endpoints中间
            app.UseAuthentication();

            //开启授权 必须放在Routing和Endpoints中间
            app.UseAuthorization();

            //执行路由
            app.UseEndpoints(config =>
            {
                config.MapHealthChecks("healthz", new HealthCheckOptions
                {
                    Predicate = _ => true,
                    ResponseWriter = UIResponseWriter.WriteHealthCheckUIResponse
                });
                config.MapHealthChecksUI();
                config.MapDefaultControllerRoute();
            });

            var job_service = app.ApplicationServices.GetRequiredService<IJobManager>();
            appLifetime.ApplicationStarted.Register(() =>
            {
                IocEx.Instance = app.ApplicationServices;
                //IocEx.Container = app.ApplicationServices.GetAutofacRoot();
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
        /// Swagger
        /// </summary>
        /// <param name="app"></param>
        private void Swagger(IApplicationBuilder app)
        {
            app.UseSwagger();
            app.UseSwaggerUI(options =>
            {
                //遍历版本号
                foreach (ApiVersions item in Enum.GetValues(typeof(ApiVersions)))
                    options.SwaggerEndpoint($"/swagger/{item.ToString()}/swagger.json", $"{item.GetDescription()}");

                options.RoutePrefix = "docs";
                options.DocumentTitle = "SSS Project";

                options.IndexStream = () => GetType().GetTypeInfo().Assembly.GetManifestResourceStream("SSS.Api.miniprofiler.html");
            });
        }

        /// <summary>
        /// 文件浏览
        /// </summary>
        /// <param name="app"></param>
        private void UseDefaultStaticFile(IApplicationBuilder app)
        {
            var contentRoot = Directory.GetCurrentDirectory();
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