using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;

using System.Threading.Tasks;

namespace SSS.Api.Seedwork.Middleware
{
    /// <summary>
    ///     UrlsMiddleware
    /// </summary>
    public class UrlsMiddleware
    {
        private readonly ILogger _logger;
        private readonly RequestDelegate _next;

        /// <summary>
        ///     UrlsMiddleware
        /// </summary>
        /// <param name="next"></param>
        /// <param name="loggerFactory"></param>
        public UrlsMiddleware(RequestDelegate next, ILoggerFactory loggerFactory)
        {
            _next = next;
            _logger = loggerFactory.CreateLogger<UrlsMiddleware>();
        }

        public async Task Invoke(HttpContext context)
        {
            if (context.Request.Path.Value.Equals("/code"))
                context.Request.Path = "/api/v1/code/index";

            if (string.IsNullOrWhiteSpace(context.Request.Path.ToString()) ||
                context.Request.Path.ToString().Equals("/"))
            {
                context.Response.ContentType = "text/html";
                await context.Response.WriteAsync("<html><body>");
                await context.Response.WriteAsync("<h1 style='margin-top:20%;text-align:center;color:red'>This's SSS FrameWork !</h1>");
                await context.Response.WriteAsync("</body></html>");
            }

            await _next.Invoke(context);
        }
    }
}