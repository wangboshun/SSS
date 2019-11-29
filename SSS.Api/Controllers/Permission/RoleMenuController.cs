using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.RoleMenu.Service;
using SSS.Domain.Permission.RoleMenu.Dto;

namespace SSS.Api.Controllers.Permission
{
    /// <summary>
    /// 菜单角色映射
    /// </summary> 
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class RoleMenuController : ApiBaseController
    {
        private readonly IRoleMenuService _service;

        /// <summary>
        /// RoleMenuController
        /// </summary>
        /// <param name="service">IRoleMenuService</param>
        public RoleMenuController(IRoleMenuService service)
        {
            _service = service;
        }

        /// <summary>
        ///     获取所有菜单角色映射信息
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns>
        [HttpGet("getlist")]
        [AllowAnonymous] //匿名访问
        public IActionResult GetList([FromQuery] RoleMenuInputDto input)
        {
            var result = _service.GetListRoleMenu(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// 获取角色下所有菜单
        /// </summary>
        /// <param name="roleid">角色Id</param>
        /// <returns></returns> 
        [HttpGet("get_menus_by_roleid")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetMenuByRole(string roleid)
        {
            var result = _service.GetMenuByRole(roleid);
            return ApiResponse(result);
        }

        /// <summary>
        /// 删除角色下的所有菜单
        /// </summary>
        /// <param name="roleid">角色Id</param>
        /// <returns></returns> 
        [HttpDelete("delete_rolemenu_by_roleid")]
        [AllowAnonymous]  //匿名访问
        public IActionResult DeleteRoleMenuByRole(string roleid)
        {
            var result = _service.DeleteRoleMenuByRole(roleid);
            return ApiResponse(result);
        }

        /// <summary>
        /// 添加角色菜单关联关系
        /// </summary>
        /// <param name="input">角色Id、菜单Id</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddRoleMenu([FromBody]RoleMenuInputDto input)
        {
            _service.AddRoleMenu(input);
            return ApiResponse(input);
        }
    }
}
