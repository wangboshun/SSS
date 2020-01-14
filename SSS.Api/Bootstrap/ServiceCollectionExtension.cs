using AutoMapper;

using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.OpenApi.Models;

using SSS.Api.Seedwork.ServiceCollection;
using SSS.Infrastructure.Seedwork.Cache.Memcached;
using SSS.Infrastructure.Seedwork.Cache.MemoryCache;
using SSS.Infrastructure.Seedwork.Cache.Redis;
using SSS.Infrastructure.Util.Enum;

using Swashbuckle.AspNetCore.Filters;

using System;
using System.IO;
using System.Linq;
using System.Reflection;

namespace SSS.Api.Bootstrap
{
    /// <summary>
    ///     ServiceCollectionExtension
    /// </summary>
    public static class ServiceCollectionExtension
    {
        /// <summary>
        ///     Service Base
        /// </summary>
        /// <param name="services"></param>
        public static void AddService(this IServiceCollection services)
        {
            // Sdk
            services.AutoRegisterServicesFromAssembly("SSS.DigitalCurrency");

            // Domain  
            services.AutoRegisterServicesFromAssembly("SSS.Domain.Seedwork");
            services.AutoRegisterServicesFromAssembly("SSS.Domain");

            // Infra 
            services.AutoRegisterServicesFromAssembly("SSS.Infrastructure.Seedwork");
            services.AutoRegisterServicesFromAssembly("SSS.Infrastructure");

            // Application
            services.AutoRegisterServicesFromAssembly("SSS.Application.Seedwork");
            services.AutoRegisterServicesFromAssembly("SSS.Application.Job");
            services.AutoRegisterServicesFromAssembly("SSS.Application");
        }

        /// <summary>
        ///     AutoMapper
        /// </summary>
        /// <param name="services"></param>
        public static void AddAutoMapperSupport(this IServiceCollection services)
        {
            if (services == null) throw new ArgumentNullException(nameof(services));

            Type[] types = Assembly.Load("SSS.Application").GetTypes().Where(t => t.BaseType != null && t.BaseType.Name.Equals("Profile")).ToArray();

            services.AddAutoMapper(types);
        }

        /// <summary>
        ///     Swagger
        /// </summary>
        /// <param name="services"></param>
        public static void AddSwagger(this IServiceCollection services)
        {
            //版本分组
            services.AddVersionedApiExplorer(option =>
            {
                option.GroupNameFormat = "'v'V";
            });

            services.AddSwaggerGen(options =>
            {
                //遍历版本号
                foreach (ApiVersions item in Enum.GetValues(typeof(ApiVersions)))
                {
                    options.SwaggerDoc(item.ToString(), new OpenApiInfo
                    {
                        Version = item.ToString(),
                        Title = item.GetDescription(),
                        Description = item.GetDescription() + "---说明文档",
                        Contact = new OpenApiContact { Name = "WBS", Email = "512742341@qq.com" }
                    });
                }

                var xmlFile = $"{Assembly.GetExecutingAssembly().GetName().Name}.xml";
                var xmlPath = Path.Combine(AppContext.BaseDirectory, xmlFile);
                options.IncludeXmlComments(xmlPath);

                // 开启加权小锁
                options.OperationFilter<AddResponseHeadersFilter>();
                options.OperationFilter<AppendAuthorizeToSummaryOperationFilter>();

                // 在header中添加token，传递到后台
                options.OperationFilter<SecurityRequirementsOperationFilter>();

                // 必须是 oauth2
                options.AddSecurityDefinition("oauth2", new OpenApiSecurityScheme
                {
                    Description = "授权:Bearer +token",
                    Name = "Authorization",//jwt默认的参数名称
                    In = ParameterLocation.Header,//jwt默认存放Authorization信息的位置(请求头中)
                    Type = SecuritySchemeType.ApiKey
                });

            });
        }

        /// <summary>
        /// ApiVersion
        /// </summary>
        /// <param name="services"></param>
        public static void AddApiVersion(this IServiceCollection services)
        {
            services.AddApiVersioning(options =>
            {
                options.ReportApiVersions = true;
                options.AssumeDefaultVersionWhenUnspecified = false;
                options.DefaultApiVersion = new ApiVersion(1, 0);
            });
        }

        #region AddMemoryCacheEx

        /// <summary>
        ///     MemoryCache扩展
        /// </summary>
        /// <param name="services"></param>
        public static void AddMemoryCacheEx(this IServiceCollection services)
        {
            services.AddMemoryCache();
            services.AddTransient<MemoryCacheEx>();
        }

        #endregion

        #region Redis

        /// <summary>
        ///     配置Redis链接
        /// </summary>
        /// <param name="services"></param>
        /// <param name="section"></param>
        public static void AddRedisCache(this IServiceCollection services, IConfigurationSection section)
        {
            services.Configure<RedisOptions>(section);
            services.AddTransient<RedisCache>();
        }

        /// <summary>
        ///     配置Redis链接
        /// </summary>
        /// <param name="services"></param>
        /// <param name="options"></param>
        public static void AddRedisCache(this IServiceCollection services, Action<RedisOptions> options)
        {
            services.Configure(options);
            services.AddTransient<RedisCache>();
        }

        /// <summary>
        ///     默认Redis链接
        /// </summary>
        /// <param name="services"></param>
        public static void AddRedisCache(this IServiceCollection services)
        {
            services.AddTransient<RedisCache>();
        }

        #endregion

        #region Memcached

        /// <summary>
        ///     配置Memcached链接
        /// </summary>
        /// <param name="services"></param>
        /// <param name="section"></param>
        public static void AddMemCached(this IServiceCollection services, IConfigurationSection section)
        {
            services.Configure<MemCachedOptions>(section);
            services.AddTransient<MemCached>();
        }

        /// <summary>
        ///     默认Memcached链接
        /// </summary>
        /// <param name="services"></param>
        public static void AddMemcached(this IServiceCollection services)
        {
            services.AddTransient<MemCached>();
        }

        /// <summary>
        ///     配置Memcached链接
        /// </summary>
        /// <param name="services"></param>
        /// <param name="options"></param>
        public static void AddMemcached(this IServiceCollection services, Action<MemCachedOptions> options)
        {
            services.Configure(options);
            services.AddTransient<RedisCache>();
        }

        #endregion
    }
}