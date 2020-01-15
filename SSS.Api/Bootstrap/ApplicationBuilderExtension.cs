using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;

using SSS.Api.Seedwork.Middleware;
using SSS.Infrastructure.Seedwork.Cache.Redis;
using SSS.Infrastructure.Util.Http;

using System;

namespace SSS.Api.Bootstrap
{
    /// <summary>
    /// ApplicationBuilderExtension
    /// </summary>
    public static class ApplicationBuilderExtension
    {
        /// <summary>
        /// 异常返回
        /// </summary>
        /// <param name="builder"></param>
        /// <returns></returns>
        public static IApplicationBuilder UseApiException(this IApplicationBuilder builder)
        {
            return builder.UseMiddleware<ApiExceptionMiddleware>();
        }

        /// <summary>
        /// 注入HttpContext
        /// </summary>
        /// <param name="app"></param>
        /// <returns></returns>
        public static IApplicationBuilder UseHttpContext(this IApplicationBuilder app)
        {
            //获取HtppContext实例
            var httpContextAccessor = app.ApplicationServices.GetRequiredService<IHttpContextAccessor>();
            //获取IHostingEnvironment实例
            var hostingEnvironment = app.ApplicationServices.GetRequiredService<IHostEnvironment>();
            //注入实例
            HttpContextService.Configure(httpContextAccessor, hostingEnvironment);
            return app;
        }

        /// <summary>
        /// 注入Memcached
        /// </summary>
        /// <param name="app"></param>
        /// <param name="options"></param>
        /// <returns></returns>
        public static IApplicationBuilder UseMemCached(this IApplicationBuilder app,
            Action<MemCachedOptions> options = null)
        {
            MemCachedOptions config = null;
            options(config);
            if (config == null)
                config = new MemCachedOptions { host = "localhost", port = 11211 };

            return app;
        }

        /// <summary>
        /// 注入RedisCache
        /// </summary>
        /// <param name="app"></param>
        /// <param name="options"></param>
        /// <returns></returns>
        public static IApplicationBuilder UseRedisCache(this IApplicationBuilder app,
            Action<RedisOptions> options = null)
        {
            RedisOptions config = null;
            options(config);
            if (config == null)
                config = new RedisOptions { host = "localhost", port = 6379 };

            return app;
        }
    }
}