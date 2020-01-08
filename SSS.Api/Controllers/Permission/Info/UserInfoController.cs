using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.Info.UserInfo.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Domain.Permission.Info.UserInfo.Dto;

namespace SSS.Api.Controllers.Permission.Info
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
        ///     用户信息
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
            return PageResponse(result);
        }

        /// <summary>
        ///     获取用户的所有权限
        /// </summary>
        /// <param name="userid">userid</param>
        /// <returns></returns>
        [HttpGet("getpermission")]
        [AllowAnonymous] //匿名访问
        public IActionResult GetPermission(string userid)
        {
            var result = _service.GetUserPermission(userid);
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
            var result = _service.AddUserInfo(input);
            return AddResponse(result);
        }

        /// <summary>
        /// 删除用户信息
        /// </summary>
        /// <param name="id">id</param>
        /// <returns></returns> 
        [HttpDelete("{id}")]
        [AllowAnonymous]  //匿名访问
        public IActionResult DeleteUserInfo(string id)
        {
            var result = _service.DeleteUserInfo(id);
            return DeleteResponse(id, result);
        }

        /// <summary>
        /// 根据用户组Id或名称，遍历关联用户
        /// </summary>
        /// <param name="input">用户组Id或名称</param>
        /// <returns></returns> 
        [HttpGet("get_user_by_usergroup")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetUserByUserGroup([FromQuery]UserGroupInputDto input)
        {
            var result = _service.GetUserByUserGroup(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联用户
        /// </summary>
        /// <param name="input">权限组Id或名称</param>
        /// <returns></returns> 
        [HttpGet("get_user_by_powergroup")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetUserByPowerGroup([FromQuery]PowerGroupInputDto input)
        {
            var result = _service.GetUserByPowerGroup(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 根据角色组Id或名称，遍历关联用户
        /// </summary>
        /// <param name="input">角色组Id或名称</param>
        /// <returns></returns> 
        [HttpGet("get_user_by_rolegroup")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetUserByRoleGroup([FromQuery]RoleGroupInputDto input)
        {
            var result = _service.GetUserByRoleGroup(input);
            return PageResponse(result);
        }
    }
}