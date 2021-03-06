using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.Group.RoleGroup.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Domain.Permission.Info.RoleInfo.Dto;
using SSS.Domain.Permission.Info.UserInfo.Dto;

namespace SSS.Api.Controllers.Permission.Group
{
    /// <summary>
    /// 角色组
    /// </summary>
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    [Authorize]
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
        /// 增加角色组
        /// </summary>
        /// <param name="input">角色组名称</param>
        /// <returns></returns>
        [HttpPost("add")]
        public IActionResult AddRoleGroup([FromBody] RoleGroupInputDto input)
        {
            var result = _service.AddRoleGroup(input);
            return AddResponse(result);
        }

        /// <summary>
        /// 删除角色组
        /// </summary>
        /// <param name="id">id</param>
        /// <returns></returns>
        [HttpDelete("{id}")]
        public IActionResult DeleteRoleGroup(string id)
        {
            var result = _service.DeleteRoleGroup(id);
            return DeleteResponse(id, result);
        }

        /// <summary>
        /// 获取所有角色组
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns>
        [HttpGet("getlist")]
        public IActionResult GetList([FromQuery] RoleGroupInputDto input)
        {
            var result = _service.GetListRoleGroup(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="input">权限组Id或名称</param>
        /// <returns></returns>
        [HttpGet("get_rolegroup_by_powergroup")]
        public IActionResult GetRoleGroupByPowerGroup([FromQuery] PowerGroupInputDto input)
        {
            var result = _service.GetRoleGroupByPowerGroup(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 根据角色Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="input">角色Id或名称</param>
        /// <returns></returns>
        [HttpGet("get_rolegroup_by_role")]
        public IActionResult GetRoleGroupByRole([FromQuery] RoleInfoInputDto input)
        {
            var result = _service.GetRoleGroupByRole(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 根据用户Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="input">用户Id或名称</param>
        /// <returns></returns>
        [HttpGet("get_rolegroup_by_user")]
        public IActionResult GetRoleGroupByUser([FromQuery] UserInfoInputDto input)
        {
            var result = _service.GetRoleGroupByUser(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 根据用户组Id或名称，遍历关联角色组
        /// </summary>
        /// <param name="input">用户组Id或名称</param>
        /// <returns></returns>
        [HttpGet("get_rolegroup_by_usergroup")]
        public IActionResult GetRoleGroupByUserGroup([FromQuery] UserGroupInputDto input)
        {
            var result = _service.GetRoleGroupByUserGroup(input);
            return PageResponse(result);
        }
    }
}