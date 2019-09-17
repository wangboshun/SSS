using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using System.Threading.Tasks;

namespace SSS.Api.Seedwork.Middleware
{
    /// <summary>
    /// UrlsMiddleware
    /// </summary>
    public class UrlsMiddleware
    {
        private readonly RequestDelegate _next;
        private readonly ILogger _logger;

        /// <summary>
        /// UrlsMiddleware
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
            {
                context.Request.Path = "/api/v1/code/index";
            }
            await _next.Invoke(context);
        }
    }
}
