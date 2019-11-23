using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.UserActivity.Service;
using SSS.Domain.UserActivity.Dto;

namespace SSS.Api.Controllers
{
    /// <summary>
    ///     UserActivityController
    /// </summary>
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class UserActivityController : ApiBaseController
    {
        private readonly IUserActivityService _service;

        /// <summary>
        ///     UserActivityController
        /// </summary>
        /// <param name="service">IUserActivityService</param>
        public UserActivityController(IUserActivityService service)
        {
            _service = service;
        }

        /// <summary>
        ///     GetList
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns>
        [HttpGet("getlist")]
        [AllowAnonymous] //匿名访问
        public IActionResult GetList([FromQuery] UserActivityInputDto input)
        {
            var result = _service.GetListUserActivity(input);
            return Response(result);
        }

        /// <summary>
        ///     GetGroupNumberByName
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns>
        [HttpGet("getgroupnumber")]
        [AllowAnonymous] //匿名访问
        public IActionResult GetGroupNumber([FromQuery] UserActivityInputDto input)
        {
            input.userid = UserInfo?.id;
            var result = _service.GetGroupNumber(input);
            return Response(result);
        }

        /// <summary>
        ///     AddUserActivity
        /// </summary>
        /// <param name="input">UserActivityInputDto</param>
        /// <returns></returns>
        [HttpPost("add")]
        [AllowAnonymous] //匿名访问
        public IActionResult AddUserActivity([FromBody] UserActivityInputDto input)
        {
            input.userid = UserInfo?.id;
            _service.AddUserActivity(input);
            return Response(input);
        }
    }
}