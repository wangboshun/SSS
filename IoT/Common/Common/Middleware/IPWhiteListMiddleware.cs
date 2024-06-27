using System.Net;

using Common.Utils;

using Furion;

using Microsoft.AspNetCore.Http;

namespace Common.Middleware;

/// <summary>
///     白名单中间件
/// </summary>
public class IPWhiteListMiddleware
{
    private readonly RequestDelegate _next;

    public IPWhiteListMiddleware(RequestDelegate next)
    {
        _next = next;
    }

    public async Task InvokeAsync(HttpContext context)
    {
        //如果关闭白名单功能
        if (!App.GetConfig<bool>("Sys:IPWhiteList:Enable"))
        {
            await _next.Invoke(context).ConfigureAwait(false);
            return;
        }

        //不监听openapi的接口
        if (!context.Request.Path.StartsWithSegments("/api"))
        {
            var ip = context.Connection.RemoteIpAddress?.MapToIPv4().ToString();
            var ips = App.GetConfig<string[]>("Sys:IPWhiteList:IP");
            //IP是否在白名单内
            if (!IPUtils.IsWhiteIP(ip, ips))
            {
                int code = (int)HttpStatusCode.Forbidden;
                context.Response.StatusCode = code;
                context.Response.ContentType = "application/json";
                await context.Response.WriteAsJsonAsync(ResponseUtils.Fail("禁止访问！", code));
            }
        }

        await _next.Invoke(context).ConfigureAwait(false);
    }
}