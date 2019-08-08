using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using Senparc.CO2NET.Extensions;
using SSS.Application.UserInfo.Service;
using SSS.Domain.UserInfo.Dto;
using SSS.Infrastructure.Seedwork.Cache.MemoryCache;
using SSS.Infrastructure.Util.Http;
using SSS.Infrastructure.Util.Json;
using System.Threading.Tasks;

namespace SSS.Api.Middware
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
            if (!context.Request.Path.Value.Contains("/api/v1/UserInfo/add") &&
                !context.Request.Path.Value.Contains("/code") &&
                !context.Request.Path.Value.Contains("/doc") &&
                !context.Request.Path.Value.Contains("/profiler/") &&
                !context.Request.Path.Value.Contains("hangfire") &&
                !context.Request.Path.Value.Contains("Trade/operatrade") &&
                !context.Request.Path.Value.Contains("swagger"))
            {
                string openid = context.Request.Headers["Auth"];

                string cachekey = "AuthUserInfo_" + openid;

                if (!string.IsNullOrWhiteSpace(openid))
                {
                    var val = _memorycache.Get<UserInfoOutputDto>(cachekey);

                    if (val != null)
                        await _next.Invoke(context);
                    else
                    {
                        IUserInfoService userinfoservice = (IUserInfoService)HttpContextService.Current.RequestServices.GetService(typeof(IUserInfoService));
                        var userinfo = userinfoservice.GetUserInfoByOpenId(openid);
                        if (userinfo == null)
                            await LoginAsync(context, 401, "无效账户,非法请求！");
                        else
                        {
                            _memorycache.Set(cachekey, userinfo, 0.2);
                            await _next.Invoke(context);
                        }
                    }
                }
                else
                    await LoginAsync(context, 401, "请授权登录！");
            }
            else
                await _next.Invoke(context);
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
