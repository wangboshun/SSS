using Common.Middleware;

using Furion;

using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;

namespace Start;

[AppStartup(1)]
public class LastStartup : AppStartup
{
    public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
    {
        app.UseResponseCompression();
        app.UseMiddleware<IPWhiteListMiddleware>();
        app.UseMiddleware<DashboardAuthMiddleware>();
        app.EnableBuffering(); // 启用Body重复读功能
        app.UseCorsAccessor();
        app.UseRouting();
        app.UseAuthentication();
        app.UseAuthorization();
        app.UseStaticFiles();
        app.UseScheduleUI();
        app.UseSpecificationDocuments();
        //跨域
        app.UseCors(options =>
        {
            options.AllowAnyHeader();
            options.AllowAnyMethod();
            options.AllowAnyOrigin();
        });
        app.Use(async (context, next) =>
        {
            context.Response.Headers.Add("X-Frame-Options", "SAMEORIGIN");
            await next.Invoke();
        });
        app.UseEndpoints(endpoints => { endpoints.MapControllers(); });
    }
}