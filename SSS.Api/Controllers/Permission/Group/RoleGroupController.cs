using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.Group.RoleGroup.Service;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Relation.RoleGroupRelation.Dto;

namespace SSS.Api.Controllers.Permission.Group
{
    /// <summary>
    /// RoleGroupController
    /// </summary> 
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class RoleGroupController : ApiBaseController
    {
        private readonly IRoleGroupService _service;

        /// <summary>
        /// 角色组
        /// </summary>
        /// <param name="service">IRoleGroupService</param>
        public RoleGroupController(IRoleGroupService service)
        {
            _service = service;
        }

        /// <summary>
        /// 获取所有角色组
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns> 
        [HttpGet("getlist")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetList([FromQuery]RoleGroupInputDto input)
        {
            var result = _service.GetListRoleGroup(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// 增加角色组
        /// </summary>
        /// <param name="input">角色组名称</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddRoleGroup([FromBody]RoleGroupInputDto input)
        {
            _service.AddRoleGroup(input);
            return ApiResponse(input);
        }

        /// <summary>
        /// 根据角色Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="input">角色Id或名称</param>
        /// <returns></returns> 
        [HttpGet("get_rolegroup_by_role")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetUserGroupByUser([FromQuery]RoleGroupRelationInputDto input)
        {
            var result = _service.GetRoleGroupByRole(input);
            return ApiResponse(result);
        }
    }
}
