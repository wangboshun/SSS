using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.RoleOperate.Service;
using SSS.Domain.Permission.RoleOperate.Dto;

namespace SSS.Api.Controllers.Permission
{
    /// <summary>
    /// 角色操作映射
    /// </summary> 
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class RoleOperateController : ApiBaseController
    {
        private readonly IRoleOperateService _service;

        /// <summary>
        /// RoleOperateController
        /// </summary>
        /// <param name="service">IRoleOperateService</param>
        public RoleOperateController(IRoleOperateService service)
        {
            _service = service;
        }

        /// <summary>
        ///     获取所有角色操作映射信息
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns>
        [HttpGet("getlist")]
        [AllowAnonymous] //匿名访问
        public IActionResult GetList([FromQuery] RoleOperateInputDto input)
        {
            var result = _service.GetListRoleOperate(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// 获取角色下的所有操作
        /// </summary>
        /// <param name="roleid">角色Id</param>
        /// <returns></returns> 
        [HttpGet("get_operates_by_roleid")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetOperateByRole(string roleid)
        {
            var result = _service.GetOperateByRole(roleid);
            return ApiResponse(result);
        }

        /// <summary>
        /// 删除角色下的所有操作
        /// </summary>
        /// <param name="roleid">角色Id</param>
        /// <returns></returns> 
        [HttpDelete("delete_roleoperate_by_roleid")]
        [AllowAnonymous]  //匿名访问
        public IActionResult DeleteRoleOperateByRole(string roleid)
        {
            var result = _service.DeleteRoleOperateByRole(roleid);
            return ApiResponse(result);
        }

        /// <summary>
        /// 添加角色操作关联关系
        /// </summary>
        /// <param name="input">角色Id、操作Id</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddRoleOperate([FromBody]RoleOperateInputDto input)
        {
            _service.AddRoleOperate(input);
            return ApiResponse(input);
        }
    }
}
