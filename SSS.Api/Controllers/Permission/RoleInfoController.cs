using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.RoleInfo.Service;
using SSS.Domain.Permission.RoleInfo.Dto;

namespace SSS.Api.Controllers.Permission
{
    /// <summary>
    /// 角色信息
    /// </summary> 
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class RoleInfoController : ApiBaseController
    {
        private readonly IRoleInfoService _service;

        /// <summary>
        /// RoleInfoController
        /// </summary>
        /// <param name="service">IRoleInfoService</param>
        public RoleInfoController(IRoleInfoService service)
        {
            _service = service;
        }

        /// <summary>
        ///     获取所有角色信息
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns>
        [HttpGet("getlist")]
        [AllowAnonymous] //匿名访问
        public IActionResult GetList([FromQuery] RoleInfoInputDto input)
        {
            var result = _service.GetListRoleInfo(input);
            return ApiResponse(result);
        }

        /// <summary>
        ///     获取角色下的所有下级
        /// </summary>
        /// <param name="roleid">角色Id</param>
        /// <returns></returns>
        [HttpGet("get_children_by_roleid")]
        [AllowAnonymous] //匿名访问
        public IActionResult GetChildren(string roleid)
        {
            var result = _service.GetChildren(roleid);
            return ApiResponse(result);
        }

        /// <summary>
        /// 添加角色信息
        /// </summary>
        /// <param name="input">角色名称</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddRoleInfo([FromBody]RoleInfoInputDto input)
        {
            _service.AddRoleInfo(input);
            return ApiResponse(input);
        }
    }
}
