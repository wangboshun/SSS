using FluentValidation.Results;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Caching.Memory;
using Microsoft.Extensions.Logging;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.UserInfo.Dto;
using SSS.Infrastructure.Util.Http;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Api.Seedwork.Controller
{
    public abstract class ApiBaseController : ControllerBase
    {
        private readonly ILogger _logger;
        private readonly IMemoryCache _memorycache;
        private readonly IErrorHandler _error;

        protected static UserInfoOutputDto UserInfo;

        protected IEnumerable<ValidationFailure> Notice
        {
            get => _error.GetNotice();
        }

        protected bool IsValidOperation()
        {
            return (!_error.HasNotice());
        }

        public ApiBaseController()
        {
            _memorycache = (IMemoryCache)HttpContextService.Current.RequestServices.GetService(typeof(IMemoryCache));
            _logger = (ILogger)HttpContextService.Current.RequestServices.GetService(typeof(ILogger<ApiBaseController>));
            _error = (IErrorHandler)HttpContextService.Current.RequestServices.GetService(typeof(IErrorHandler));

            var userinfo = _memorycache.Get<UserInfoOutputDto>("AuthUserInfo_" + HttpContextService.Current.Request.Headers["Auth"]);
            if (userinfo != null && UserInfo == null)
                UserInfo = userinfo;
        }

        protected new IActionResult Response(object data, bool status = true, string message = "", int code = 200)
        {
            if (!status)
                return Accepted(new { status = status, data = "", message = "处理失败", code = 202 });

            if (IsValidOperation())
            {
                if (data == null)
                    return Accepted(new { status = false, data = "", message = "数据为空", code = 200 });
                return Ok(new { status, data, message, code });
            }
            else
                return BadRequest(new { status = false, data = "", message = Notice.Select(n => n.ErrorMessage), code = 403 });
        }
    }
}
