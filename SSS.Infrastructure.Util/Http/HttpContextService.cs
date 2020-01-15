using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Hosting;

using System;

namespace SSS.Infrastructure.Util.Http
{
    public static class HttpContextService
    {
        private static IHttpContextAccessor _accessor;

        public static IServiceProvider ServiceProvider;

        private static IHostEnvironment _hostingEnvironment;

        public static HttpContext Current => _accessor.HttpContext;

        public static void Configure(IHttpContextAccessor accessor)
        {
            _accessor = accessor;
        }

        public static void Configure(IHttpContextAccessor accessor, IHostEnvironment hostingEnvironment)
        {
            _accessor = accessor;
            _hostingEnvironment = hostingEnvironment;
        }

        public static string ContentRootPath(this HttpContext context)
        {
            return _hostingEnvironment.ContentRootPath;
        }

        public static string WebRootPath(this HttpContext context)
        {
            return _hostingEnvironment.ContentRootPath;
        }
    }
}