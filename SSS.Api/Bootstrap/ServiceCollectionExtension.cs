using AutoMapper;

using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.OpenApi.Models;

using SSS.Api.Seedwork.ServiceCollection;
using SSS.Infrastructure.Seedwork.Cache.Memcached;
using SSS.Infrastructure.Seedwork.Cache.MemoryCache;
using SSS.Infrastructure.Seedwork.Cache.Redis;
using SSS.Infrastructure.Util.Enum;
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
            // HttpContext
            services.AddSingleton<IHttpContextAccessor, HttpContextAccessor>();

            // Sdk
            services.AutoRegisterServicesFromAssembly("SSS.DigitalCurrency");

            // Domain  
            services.AutoRegisterServicesFromAssembly("SSS.Domain.Seedwork");
            services.AutoRegisterServicesFromAssembly("SSS.Domain");

            // Application
            services.AutoRegisterServicesFromAssembly("SSS.Application.Seedwork");
            services.AutoRegisterServicesFromAssembly("SSS.Application");

            // Infra 
            services.AutoRegisterServicesFromAssembly("SSS.Infrastructure.Seedwork");
            services.AutoRegisterServicesFromAssembly("SSS.Infrastructure");
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
                typeof(ApiVersions).GetEnumNames().ToList().ForEach(version =>
                {
                    options.SwaggerDoc(version, new OpenApiInfo
                    {
                        Version = version,
                        Title = GetVersion(version),
                        Description = GetVersion(version) + "---说明文档",
                        Contact = new OpenApiContact { Name = "WBS", Email = "512742341@qq.com" }
                    });
                });

                var xmlFile = $"{Assembly.GetExecutingAssembly().GetName().Name}.xml";
                var xmlPath = Path.Combine(AppContext.BaseDirectory, xmlFile);
                options.IncludeXmlComments(xmlPath);
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