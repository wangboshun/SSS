using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.Info.RoleInfo.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Domain.Permission.Info.RoleInfo.Dto;
using SSS.Domain.Permission.Info.UserInfo.Dto;

namespace SSS.Api.Controllers.Permission.Info
{
    /// <summary>
    ///     角色信息
    /// </summary>
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    [Authorize]
    public class RoleInfoController : ApiBaseController
    {
        private readonly IRoleInfoService _service;

        /// <summary>
        ///     角色信息
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
        public IActionResult GetList([FromQuery] RoleInfoInputDto input)
        {
            var result = _service.GetListRoleInfo(input);
            return PageResponse(result);
        }

        /// <summary>
        ///     获取角色下的所有下级
        /// </summary>
        /// <param name="roleid">角色Id</param>
        /// <returns></returns>
        [HttpGet("get_children_by_roleid")]
        public IActionResult GetChildren(string roleid)
        {
            var result = _service.GetChildren(roleid);
            return ApiResponse(result);
        }

        /// <summary>
        ///     添加角色信息
        /// </summary>
        /// <param name="input">角色名称</param>
        /// <returns></returns>
        [HttpPost("add")]
        public IActionResult AddRoleInfo([FromBody] RoleInfoInputDto input)
        {
            var result = _service.AddRoleInfo(input);
            return AddResponse(result);
        }

        /// <summary>
        ///     删除角色信息
        /// </summary>
        /// <param name="id">id</param>
        /// <returns></returns>
        [HttpDelete("{id}")]
        public IActionResult DeletePowerInfo(string id)
        {
            var result = _service.DeleteRoleInfo(id);
            return DeleteResponse(id, result);
        }

        /// <summary>
        ///     根据角色组Id或名称，遍历关联角色
        /// </summary>
        /// <param name="input">角色组Id或名称</param>
        /// <returns></returns>
        [HttpGet("get_role_by_rolegroup")]
        public IActionResult GetRoleByRoleGroup([FromQuery] RoleGroupInputDto input)
        {
            var result = _service.GetRoleByRoleGroup(input);
            return PageResponse(result);
        }

        /// <summary>
        ///     根据权限组Id或名称，遍历关联角色
        /// </summary>
        /// <param name="input">权限组Id或名称</param>
        /// <returns></returns>
        [HttpGet("get_role_by_powergroup")]
        public IActionResult GetRoleByPowerGroup([FromQuery] PowerGroupInputDto input)
        {
            var result = _service.GetRoleByPowerGroup(input);
            return PageResponse(result);
        }

        /// <summary>
        ///     根据用户组Id或名称，遍历关联角色
        /// </summary>
        /// <param name="input">用户组Id或名称</param>
        /// <returns></returns>
        [HttpGet("get_role_by_usergroup")]
        public IActionResult GetRoleByUserGroup([FromQuery] UserGroupInputDto input)
        {
            var result = _service.GetRoleByUserGroup(input);
            return PageResponse(result);
        }

        /// <summary>
        ///     根据用户Id或名称，遍历关联角色
        /// </summary>
        /// <param name="input">用户Id或名称</param>
        /// <returns></returns>
        [HttpGet("get_role_by_user")]
        public IActionResult GetRoleByUser([FromQuery] UserInfoInputDto input)
        {
            var result = _service.GetRoleByUser(input);
            return PageResponse(result);
        }
    }
}