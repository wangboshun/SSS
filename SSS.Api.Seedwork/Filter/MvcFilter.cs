using Microsoft.AspNetCore.Mvc.Filters;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

namespace SSS.Api.Seedwork.Filter
{
    public class MvcFilter : IActionFilter, IResultFilter, IAuthorizationFilter, IExceptionFilter
    {
        private readonly IHostEnvironment _env;
        private readonly ILogger _logger;

        public MvcFilter(ILogger<MvcFilter> logger, IHostEnvironment env)
        {
            _logger = logger;
            _env = env;
        }

        //2
        public void OnActionExecuting(ActionExecutingContext context)
        {
        }

        //3
        public void OnActionExecuted(ActionExecutedContext context)
        {
        }

        //1
        public void OnAuthorization(AuthorizationFilterContext context)
        {
            //foreach (var item in context.HttpContext.Request.Headers)
            //{
            //    _logger.LogInformation("Headers :【" + item.Key + "】" + "  【" + item.Value + "】");
            //}
        }

        //4
        public void OnException(ExceptionContext context)
        {
            _logger.LogError(new EventId(context.Exception.HResult), context.Exception,
                context.HttpContext.Request.Path);
        }

        //5
        public void OnResultExecuting(ResultExecutingContext context)
        {
        }

        //6
        public void OnResultExecuted(ResultExecutedContext context)
        {
        }
    }
}