using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using SSS.Infrastructure.Util.Json;
using System;
using System.Threading.Tasks;

namespace SSS.Api.Middware
{
    /// <summary>
    /// ApiExceptionMiddleware
    /// </summary>
    public class ApiExceptionMiddleware
    {
        private readonly RequestDelegate next;
        private readonly ILogger _logger;

        /// <summary>
        /// ApiExceptionMiddleware
        /// </summary>
        /// <param name="next"></param>
        /// <param name="loggerFactory"></param>
        public ApiExceptionMiddleware(RequestDelegate next, ILoggerFactory loggerFactory)
        {
            this.next = next;
            _logger = loggerFactory.CreateLogger<ApiExceptionMiddleware>();
        }

        public async Task Invoke(HttpContext context)
        {
            try
            {
                await next(context);
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, context.Request.Path);
                await HandleExceptionAsync(context, 500, ex.Message);
            }
        }

        private static Task HandleExceptionAsync(HttpContext context, int code, string msg)
        {
            var data = new { status = false, data = "内部异常", message = msg, code = code };
            var result = data.ToJson();
            context.Response.ContentType = "application/json;charset=utf-8";
            return context.Response.WriteAsync(result);
        }
    }
}
