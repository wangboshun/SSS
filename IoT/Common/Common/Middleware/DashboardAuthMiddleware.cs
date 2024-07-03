using Furion;
using Microsoft.AspNetCore.Http;
using System.Net;
using System.Net.Http.Headers;
using System.Text;

namespace Common.Middleware;

/// <summary>
///     控制台仪表盘认证中间件
///     主要用于swagger和schedule的认证
/// </summary>
public class DashboardAuthMiddleware
{
    private readonly RequestDelegate _next;

    public DashboardAuthMiddleware(RequestDelegate next)
    {
        _next = next;
    }

    public async Task InvokeAsync(HttpContext context)
    {
        if (context.Request.Path.StartsWithSegments("/docs") || context.Request.Path.StartsWithSegments("/swagger"))
        {
            string authHeader = context.Request.Headers["Authorization"];
            if (authHeader != null && authHeader.StartsWith("Basic "))
            {
                var header = AuthenticationHeaderValue.Parse(authHeader);
                var inBytes = Convert.FromBase64String(header.Parameter);
                var credentials = Encoding.UTF8.GetString(inBytes).Split(':');
                var username = credentials[0];
                var password = credentials[1];
                var u = App.GetConfig<string>("Sys:SwaggerAuth:UserName");
                var p = App.GetConfig<string>("Sys:SwaggerAuth:Password");

                if (username.Equals(u) && password.Equals(p))
                {
                    await _next.Invoke(context).ConfigureAwait(false);
                    return;
                }
            }

            context.Response.Headers["WWW-Authenticate"] = "Basic";
            context.Response.StatusCode = (int)HttpStatusCode.Unauthorized;
        }
        else if (context.Request.Path.StartsWithSegments("/schedule"))
        {
            string authHeader = context.Request.Headers["Authorization"];
            if (authHeader != null && authHeader.StartsWith("Basic "))
            {
                var header = AuthenticationHeaderValue.Parse(authHeader);
                var inBytes = Convert.FromBase64String(header.Parameter);
                var credentials = Encoding.UTF8.GetString(inBytes).Split(':');
                var username = credentials[0];
                var password = credentials[1];
                var u = App.GetConfig<string>("Sys:ScheduleAuth:UserName");
                var p = App.GetConfig<string>("Sys:ScheduleAuth:Password");

                if (username.Equals(u) && password.Equals(p))
                {
                    await _next.Invoke(context).ConfigureAwait(false);
                    return;
                }
            }

            context.Response.Headers["WWW-Authenticate"] = "Basic";
            context.Response.StatusCode = (int)HttpStatusCode.Unauthorized;
        }
        else
        {
            await _next.Invoke(context).ConfigureAwait(false);
        }
    }
}