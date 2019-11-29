using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.UserInfo.Service;
using SSS.Domain.Permission.UserInfo.Dto;

namespace SSS.Api.Controllers.Permission
{
    /// <summary>
    ///     用户信息
    /// </summary>
    [ApiVersion("2.0")]
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
        ///     获取所有用户信息
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
        ///     获取用户下的所有下级
        /// </summary>
        /// <param name="userid">用户Id</param>
        /// <returns></returns>
        [HttpGet("get_children_by_userid")]
        [AllowAnonymous] //匿名访问
        public IActionResult GetChildrenById(string userid)
        {
            var result = _service.GetChildrenById(userid);
            return ApiResponse(result);
        }

        /// <summary>
        ///     获取用户所有权限：菜单、操作权限
        /// </summary>
        /// <param name="userid">用户Id</param>
        /// <returns></returns>
        [HttpGet("get_permission_by_userid")]
        [AllowAnonymous] //匿名访问
        public IActionResult GetUserPermission(string userid)
        {
            var result = _service.GetUserPermission(userid);
            return ApiResponse(result);
        }

        /// <summary>
        ///     删除用户所有权限：菜单、操作权限
        /// </summary>
        /// <param name="userid">用户Id</param>
        /// <returns></returns>
        [HttpDelete("delete_permission_by_userid")]
        [AllowAnonymous] //匿名访问
        public IActionResult DeleteUserPermission(string userid)
        {
            var result = _service.DeleteUserPermission(userid);
            return ApiResponse(result);
        }

        /// <summary>
        /// 用户登录
        /// </summary>
        /// <param name="input">账号、密码</param>
        /// <returns></returns>
        [HttpPost("login")]
        [AllowAnonymous] //匿名访问
        public IActionResult Login([FromBody] UserInfoInputDto input)
        {
            var result = _service.GetByUserName(input);
            return ApiResponse(result);
        }

        /// <summary>
        ///  添加用户
        /// </summary>
        /// <param name="input">账号、密码</param>
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