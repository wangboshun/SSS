using Common.Utils;

using Furion;
using Furion.Authorization;
using Furion.DataEncryption;

using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;

namespace Common.Ext.Jwt;

/// <summary>
/// JWT 授权自定义处理程序
/// </summary>
public class JwtHandler : AppAuthorizeHandler
{
    /// <summary>
    /// 自动刷新Token
    /// </summary>
    /// <param name="context"></param>
    /// <returns></returns>
    public override async Task HandleAsync(AuthorizationHandlerContext context, DefaultHttpContext httpContext)
    {
        var ips = App.GetConfig<string[]>("Sys:AuthorizeWhiteList:IP");
        var ip = context.GetCurrentHttpContext().GetRemoteIpAddressToIPv4();
        if (ips != null && IPUtils.IsWhiteIP(ip, ips))
        {
            await AuthorizeHandleAsync(context);
        }
        else
        {
            if (JWTEncryption.AutoRefreshToken(context, context.GetCurrentHttpContext()))
            {
                await AuthorizeHandleAsync(context);
            }
            else
            {
                context.Fail(); // 授权失败 
            }
        }
    }

    /// <summary>
    /// 权限校验核心逻辑
    /// </summary>
    /// <param name="httpContext"></param>
    /// <returns></returns>
    public override Task<bool> PipelineAsync(AuthorizationHandlerContext context, DefaultHttpContext httpContext)
    {
        // 检查权限，如果方法是异步的就不用 Task.FromResult 包裹，直接使用 async/await 即可
        return Task.FromResult(true);
    }
}