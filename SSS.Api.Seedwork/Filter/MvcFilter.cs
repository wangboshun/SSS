using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using Microsoft.Extensions.Logging;
using SSS.Domain.Seedwork.Notice;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Api.Seedwork.Filter
{
    public class MvcFilter : IActionFilter, IResultFilter, IAuthorizationFilter, IExceptionFilter
    {
        private readonly ILogger _logger;
        private readonly IHostingEnvironment _env;

        public MvcFilter(ILogger<MvcFilter> logger, IHostingEnvironment env)
        {
            _logger = logger;
            _env = env;
        }

        //1
        public void OnAuthorization(AuthorizationFilterContext context)
        {
            foreach (var item in context.HttpContext.Request.Headers)
            {
                _logger.LogInformation("Headers :【" + item.Key + "】" + "  【" + item.Value + "】");
            }
        }

        //2
        public void OnActionExecuting(ActionExecutingContext context)
        {
            //绑定验证
            if (!context.ModelState.IsValid)
            {
                List<ErrorNotice> errorResults = new List<ErrorNotice>();
                foreach (var item in context.ModelState)
                {
                    var result = new ErrorNotice(item.Key, "");

                    foreach (var error in item.Value.Errors)
                    {
                        if (!string.IsNullOrEmpty(result.Value))
                        {
                            result.Value += "  ,  ";
                        }
                        result.Value += error.ErrorMessage;
                    }
                    errorResults.Add(result);
                }
                context.Result = new BadRequestObjectResult(new { status = false, data = "", message = errorResults.Select(n => n.Value), code = 403 });
            }
        }

        //3
        public void OnActionExecuted(ActionExecutedContext context)
        {

        }

        //4
        public void OnException(ExceptionContext context)
        {
            _logger.LogError(new EventId(context.Exception.HResult), context.Exception, context.HttpContext.Request.Path);
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
