using MediatR;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Notice;
using SSS.Infrastructure.Util.Http;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Api.Seedwork
{
    public abstract class ApiBaseController : ControllerBase
    {
        private static ILogger _logger;
        private static ErrorNoticeHandler _Notice;
        private static IEventBus _mediator;

        protected IEnumerable<ErrorNotice> Notice
        {
            get => _Notice.GetNotice();
        }

        protected bool IsValidOperation()
        {
            return (!_Notice.HasNotice());
        }

        protected new IActionResult Response(object data, bool status = true, string message = "", int code = 200)
        {
            _logger = (ILogger)HttpContextService.Current.RequestServices.GetService(typeof(ILogger<ApiBaseController>));
            _Notice = (ErrorNoticeHandler)HttpContextService.Current.RequestServices.GetService(typeof(INotificationHandler<ErrorNotice>));
            _mediator = (IEventBus)HttpContextService.Current.RequestServices.GetService(typeof(IEventBus));

            if (!status)
                return Accepted(new { status = status, data = "", message = "处理失败", code = 202 });

            if (IsValidOperation())
            {
                if (data == null)
                    return Accepted(new { status = false, data = "", message = "数据为空", code = 204 });
                return Ok(new { status, data, message, code });
            }
            else
                return BadRequest(new { status = false, data = "", message = Notice.Select(n => n.Value), code = 400 });
        }
    }
}
