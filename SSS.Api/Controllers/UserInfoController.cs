using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.UserInfo.Service;
using SSS.Domain.UserInfo.Dto;

namespace SSS.Api.Controllers
{
    /// <summary>
    ///     UserInfoController
    /// </summary>
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class UserInfoController : ApiBaseController
    {
        private readonly IUserInfoService _service;

        /// <summary>
        ///     UserInfoController
        /// </summary>
        /// <param name="service">IUserInfoService</param>
        public UserInfoController(IUserInfoService service)
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
        public IActionResult GetList([FromQuery] UserInfoInputDto input)
        {
            var result = _service.GetListUserInfo(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// login
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        [HttpPost("login")]
        [AllowAnonymous] //匿名访问
        public IActionResult Login([FromBody] UserInfoInputDto input)
        {
            var result = _service.GetByUserName(input);
            return ApiResponse(result);
        }

        /// <summary>
        ///     AddUserInfo
        /// </summary>
        /// <param name="input">UserInfoInputDto</param>
        /// <returns></returns>
        [HttpPost("add")]
        [AllowAnonymous] //匿名访问
        public IActionResult AddUserInfo([FromBody] UserInfoInputDto input)
        {
            _service.AddUserInfo(input);
            return ApiResponse(input);
        }
    }
}