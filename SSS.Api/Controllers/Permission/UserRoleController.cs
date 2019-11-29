using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.UserRole.Service;
using SSS.Domain.Permission.UserRole.Dto;

namespace SSS.Api.Controllers.Permission
{
    /// <summary>
    /// 用户角色映射
    /// </summary> 
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class UserRoleController : ApiBaseController
    {
        private readonly IUserRoleService _service;

        /// <summary>
        /// UserRoleController
        /// </summary>
        /// <param name="service">IUserRoleService</param>
        public UserRoleController(IUserRoleService service)
        {
            _service = service;
        }

        /// <summary>
        ///     获取所有用户角色映射信息
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns>
        [HttpGet("getlist")]
        [AllowAnonymous] //匿名访问
        public IActionResult GetList([FromQuery] UserRoleInputDto input)
        {
            var result = _service.GetListUserRole(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// 获取角色下所有用户信息
        /// </summary>
        /// <param name="roleid">角色Id</param>
        /// <returns></returns> 
        [HttpGet("get_users_by_roleid")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetOperateByRole(string roleid)
        {
            var result = _service.GetUserByRole(roleid);
            return ApiResponse(result);
        }

        /// <summary>
        /// 删除角色下的所有用户
        /// </summary>
        /// <param name="roleid">角色Id</param>
        /// <returns></returns> 
        [HttpDelete("delete_userrole_by_roleid")]
        [AllowAnonymous]  //匿名访问
        public IActionResult DeleteUserRoleByRole(string roleid)
        {
            var result = _service.DeleteUserRoleByRole(roleid);
            return ApiResponse(result);
        }

        /// <summary>
        /// 添加用户角色关系
        /// </summary>
        /// <param name="input">用户Id、角色Id</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddUserRole([FromBody]UserRoleInputDto input)
        {
            _service.AddUserRole(input);
            return ApiResponse(input);
        }
    }
}
