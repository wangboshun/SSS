using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using SSS.Infrastructure.Util.Json;
using System;
using System.Threading.Tasks;

namespace SSS.Api.Middware
{
    public class LoginMiddleware
    {
        private readonly RequestDelegate _next;
        private readonly ILogger _logger;

        public LoginMiddleware(RequestDelegate next, ILoggerFactory loggerFactory)
        {
            _next = next;
            _logger = loggerFactory.CreateLogger<ApiExceptionMiddleware>();
        }

        public async Task Invoke(HttpContext context)
        {
            if (!context.Request.Path.Value.Contains("/api/v1/UserInfo/add") &&
                !context.Request.Path.Value.Contains("/code") &&
                !context.Request.Path.Value.Contains("/doc") &&
                !context.Request.Path.Value.Contains("/profiler/") &&
                !context.Request.Path.Value.Contains("/swagger"))
            {
                string openid = context.Request.Headers["Auth"];
                if (!string.IsNullOrWhiteSpace(openid))
                    await _next.Invoke(context);
                else
                    await LoginAsync(context, 401, "请授权微信登录！");
            }
            else
            {
                await _next.Invoke(context);
            }
        }

        private static Task LoginAsync(HttpContext context, int code, string msg)
        {
            var data = new { status = false, data = "登录错误，权限不足", message = msg, code = code };
            var result = data.ToJson();
            context.Response.ContentType = "application/json;charset=utf-8";
            return context.Response.WriteAsync(result);
        }
    }
}
