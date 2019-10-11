using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Caching.Memory;
using Microsoft.Extensions.Logging;
using SSS.Domain.UserInfo.Dto;
using SSS.Infrastructure.Util.Http;

namespace SSS.Api.Seedwork.Controller
{
    public abstract class ApiBaseController : ControllerBase
    {
        private static ILogger _logger;
        protected static UserInfoOutputDto UserInfo;
        private readonly IMemoryCache _memorycache;

        public ApiBaseController()
        {
            _memorycache = (IMemoryCache)HttpContextService.Current.RequestServices.GetService(typeof(IMemoryCache));
            var userinfo = _memorycache.Get<UserInfoOutputDto>("AuthUserInfo_" + HttpContextService.Current.Request.Headers["Auth"]);
            if (userinfo != null && UserInfo == null)
                UserInfo = userinfo;
        }

        protected new IActionResult Response(object data, bool status = true, string message = "", int code = 200)
        {
            _logger = (ILogger)HttpContextService.Current.RequestServices.GetService(typeof(ILogger<ApiBaseController>));

            if (!status)
                return Accepted(new { status = status, data = "", message = "处理失败", code = 202 });

            if (data == null)
                return Accepted(new { status = false, data = "", message = "数据为空", code = 200 });
            return Ok(new { status, data, message, code });
        }
    }
}
