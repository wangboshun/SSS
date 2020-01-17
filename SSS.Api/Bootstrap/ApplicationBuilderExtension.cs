using Microsoft.AspNetCore.Builder;

using SSS.Api.Seedwork.Middleware;
using SSS.Infrastructure.Seedwork.Cache.Redis;

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