using FluentValidation.Results;

using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Caching.Memory;
using Microsoft.Extensions.Logging;

using SSS.Domain.Permission.Info.UserInfo.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Util.Http;

using System.Collections.Generic;
using System.Linq;

/*
1xx，属于信息性的状态码。Web API并不使用1xx的状态码。 

2xx，意味着请求执行的很成功。 
    200 - Ok，表示请求成功； 
    201 - Created，请求成功并创建了资源； 
    204 - No Content，请求成功，但是不应该返回任何东西，例如删除操作。 

3xx，用于跳转。例如告诉搜素引擎，某个页面的网址已经永久的改变了。绝大多数的Web API都不需要使用这类状态码。 

4xx，客户端错误： 
    400 - Bad Request，表示API消费者发送到服务器的请求是有错误的； 
    401 - Unauthorized，表示没有提供授权信息或者提供的授权信息不正确； 
    403 - Forbidden，表示身份认证已经成功，但是已认证的用户却无法访问请求的资源； 
    404 - Not Found，表示请求的资源不存在； 
    405 - Method not allowed，当尝试发送请求到资源的时候，使用了不被支持的HTTP方法时，就会返回405状态码； 
    406 - Not acceptable，这表示API消费者请求的表述格式并不被Web API所支持，并且API不会提供默认的表述格式。例如请求的媒体类型是application/xml，但是Web API仅支持application/json类型，并且API不会将application/json作为默认格式提供； 
    409 - Conflict，表示请求与服务器当前状态冲突。通常指更新资源时发生的冲突，例如，当你编辑某个资源的时候，该资源在服务器上又进行了更新，所以你编辑的资源版本和服务器的不一致。当然有时候也用来表示你想要创建的资源在服务器上已经存在了。它就是用来处理并发问题的状态码。  
    415 - Unsupported media type，与406正好相反，有一些请求必须带着数据发往服务器，这些数据都属于特定的媒体类型，如果API不支持该媒体类型格式，415就会被返回。 
    422 - Unprocessable entity，它是HTTP扩展协议的一部分。它说明服务器已经懂得了实体的Content Type，也就是说415状态码肯定不合适；此外，实体的语法也没有问题，所以400也不合适。但是服务器仍然无法处理这个实体数据，这时就可以返回422。所以它通常是用来表示语意上有错误，通常就表示实体验证的错误。 

5xx，服务器错误： 
    500 - Internal server error，表示服务器出现了错误，客户端无能为力，只能以后再试试了。 
 */

namespace SSS.Api.Seedwork.Controller
{
    public abstract class ApiBaseController : ControllerBase
    {
        protected static UserInfoOutputDto UserInfo;
        private readonly IErrorHandler _error;
        private readonly ILogger _logger;
        private readonly IMemoryCache _memorycache;

        public ApiBaseController()
        {
            _memorycache = (IMemoryCache)HttpContextService.Current.RequestServices.GetService(typeof(IMemoryCache));
            _logger = (ILogger)HttpContextService.Current.RequestServices.GetService(typeof(ILogger<ApiBaseController>));
            _error = (IErrorHandler)HttpContextService.Current.RequestServices.GetService(typeof(IErrorHandler));

            var userinfo = _memorycache.Get<UserInfoOutputDto>("AuthUserInfo_" + HttpContextService.Current.Request.Headers["Auth"]);
            if (userinfo != null && UserInfo == null)
                UserInfo = userinfo;
        }

        protected IEnumerable<ValidationFailure> Notice
        {
            get => _error.GetNotice();
        }

        protected bool IsValidOperation()
        {
            return !_error.HasNotice();
        }

        /// <summary>
        /// 增加
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="data"></param>
        /// <returns></returns>
        protected IActionResult AddResponse<T>(T data) where T : class
        {
            if (data != null)
                return ApiResponse(data, true, "新增成功", 200);
            return ApiResponse(data, false, "新增失败", 204);
        }

        /// <summary>
        /// 删除
        /// </summary>
        /// <param name="data"></param>
        /// <param name="status"></param>
        /// <returns></returns>
        protected IActionResult DeleteResponse(object data, bool status)
        {
            if (data != null & status)
                return ApiResponse(data, true, "删除成功", 200);
            return ApiResponse(data, false, "删除失败", 204);
        }

        /// <summary>
        /// 修改
        /// </summary>
        /// <param name="data"></param>
        /// <param name="status"></param>
        /// <returns></returns>
        protected IActionResult UpdateResponse(object data, bool status)
        {
            if (data != null & status)
                return ApiResponse(data, true, "修改成功", 200);
            return ApiResponse(data, false, "修改失败", 204);
        }

        /// <summary>
        ///分页 
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="data"></param>
        /// <returns></returns>
        protected IActionResult PageResponse<T>(Pages<List<T>> data) where T : OutputDtoBase
        {
            if (data != null)
            {
                if (data.count > 0)
                    return ApiResponse(data, true, "获取分页成功", 200);
                else
                    return ApiResponse(null, true, "分页数据为空", 200);
            }
            return ApiResponse(null, false, "获取分页失败", 204);
        }

        /// <summary>
        /// 基础
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="data"></param>
        /// <returns></returns>
        protected IActionResult ApiResponse<T>(T data) where T : class
        {
            if (data != null)
                return ApiResponse(data, true, "操作成功", 200);
            return ApiResponse(null, false, "操作失败", 204);
        }

        /// <summary>
        /// 其他
        /// </summary>
        /// <param name="status"></param>
        /// <returns></returns>
        protected IActionResult ApiResponse(bool status)
        {
            if (status)
                return ApiResponse(true, true, "操作成功", 200);
            return ApiResponse(true, false, "操作失败", 204);
        }

        protected IActionResult ApiResponse(object data, bool status, string message, int code)
        {
            if (!status)
            {
                if (!IsValidOperation())
                    return BadRequest(new { status = false, data = "", message = Notice.Select(n => n.ErrorMessage), code = code });

                return Accepted(new { status, data, message, code });
            }

            if (IsValidOperation())
            {
                if (data == null)
                    return Accepted(new { status = false, data = "", message = message, code = code });
                return Ok(new { status, data, message, code });
            }

            return BadRequest(new { status = false, data = "", message = Notice.Select(n => n.ErrorMessage), code = code });
        }
    }
}