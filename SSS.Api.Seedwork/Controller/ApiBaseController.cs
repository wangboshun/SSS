using MediatR;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Caching.Memory;
using Microsoft.Extensions.Logging;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Notice;
using SSS.Infrastructure.Util.Http;
using System.Collections.Generic;
using System.Linq;
using SSS.Domain.UserInfo;
using SSS.Domain.UserInfo.Dto;

namespace SSS.Api.Seedwork.Controller
{
    public abstract class ApiBaseController : ControllerBase
    {
        private static ILogger _logger;
        private static ErrorNoticeHandler _Notice;
        private static IEventBus _mediator;
        protected static UserInfoOutputDto UserInfo; 
        private readonly IMemoryCache _memorycache;

        public ApiBaseController()
        {
            _memorycache = (IMemoryCache)HttpContextService.Current.RequestServices.GetService(typeof(IMemoryCache));
            var userinfo = _memorycache.Get<UserInfoOutputDto>("AuthUserInfo_" + HttpContextService.Current.Request.Headers["Auth"]);
            if (userinfo != null && UserInfo == null)
                UserInfo = userinfo;
        }

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
                    return Accepted(new { status = false, data = "", message = "数据为空", code = 200 });
                return Ok(new { status, data, message, code });
            }
            else
                return BadRequest(new { status = false, data = "", message = Notice.Select(n => n.Value), code = 403 });
        }
    }
}
