using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using SSS.Domain.UserInfo.Dto;
using SSS.Infrastructure.Seedwork.Cache.MemoryCache;
using SSS.Infrastructure.Util.Json;
using System.Threading.Tasks;

namespace SSS.Api.Seedwork.Middleware
{
    public class LoginMiddleware
    {
        private readonly RequestDelegate _next;
        private readonly ILogger _logger;
        private readonly MemoryCacheEx _memorycache;

        public LoginMiddleware(RequestDelegate next, ILoggerFactory loggerFactory, MemoryCacheEx memorycache)
        {
            _next = next;
            _memorycache = memorycache;
            _logger = loggerFactory.CreateLogger<ApiExceptionMiddleware>();
        }

        public async Task Invoke(HttpContext context)
        {
            string userid = context.Request.Headers["Auth"];
            string cachekey = "AuthUserInfo_" + userid;

            //如果是登录
            if (context.Request.Path.Value.Contains("/api/v1/UserInfo/add"))
                _memorycache.Remove(cachekey);

            if (!context.Request.Path.Value.Contains("/api/v1/UserInfo/add") &&
                !context.Request.Path.Value.Contains("/api/v1/UserInfo/login") &&
                !context.Request.Path.Value.Contains("/code") &&
                !context.Request.Path.Value.Contains("/createcode") &&
                !context.Request.Path.Value.Contains("/File/") &&
                !context.Request.Path.Value.Contains("/file") &&
                !context.Request.Path.Value.Contains("/doc") &&
                !context.Request.Path.Value.Contains("/profiler/") &&
                !context.Request.Path.Value.Contains("hangfire") &&
                !context.Request.Path.Value.Contains("swagger"))
            {
                if (!string.IsNullOrWhiteSpace(userid))
                {
                    var val = _memorycache.Get<UserInfoOutputDto>(cachekey);

                    if (val != null)
                        await _next.Invoke(context);
                    else
                        await LoginAsync(context, 401, "登录超时，重新登录！");
                }
                else
                    await LoginAsync(context, 401);
            }
            else
                await _next.Invoke(context);
        }

        private static Task LoginAsync(HttpContext context, int code, string msg = "请求失败，权限不足！")
        {
            var data = new { status = false, data = msg, message = msg, code = code };
            var result = data.ToJson();
            context.Response.Headers["Access-Control-Allow-Origin"] = "*";
            context.Response.ContentType = "application/json;charset=utf-8";
            return context.Response.WriteAsync(result);
        }
    }
}