using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.Group.UserGroup.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Domain.Permission.Info.UserInfo.Dto;

namespace SSS.Api.Controllers.Permission.Group
{
    /// <summary>
    /// 用户组
    /// </summary>
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    [Authorize]
    public class UserGroupController : ApiBaseController
    {
        private readonly IUserGroupService _service;

        /// <summary>
        /// 用户组
        /// </summary>
        /// <param name="service">IUserGroupService</param>
        public UserGroupController(IUserGroupService service)
        {
            _service = service;
        }

        /// <summary>
        /// 增加用户组
        /// </summary>
        /// <param name="input">用户组名称</param>
        /// <returns></returns>
        [HttpPost("add")]
        public IActionResult AddUserGroup([FromBody] UserGroupInputDto input)
        {
            var result = _service.AddUserGroup(input);
            return AddResponse(result);
        }

        /// <summary>
        /// 删除用户组
        /// </summary>
        /// <param name="id">id</param>
        /// <returns></returns>
        [HttpDelete("{id}")]
        public IActionResult DeleteUserGroup(string id)
        {
            var result = _service.DeleteUserGroup(id);
            return DeleteResponse(id, result);
        }

        /// <summary>
        /// 获取所有用户组
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns>
        [HttpGet("getlist")]
        public IActionResult GetList([FromQuery] UserGroupInputDto input)
        {
            var result = _service.GetListUserGroup(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联用户组
        /// </summary>
        /// <param name="input">权限组Id或名称</param>
        /// <returns></returns>
        [HttpGet("get_usergroup_by_powergroup")]
        public IActionResult GetUserGroupByPowerGroup([FromQuery] PowerGroupInputDto input)
        {
            var result = _service.GetUserGroupByPowerGroup(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 根据角色组Id或名称，遍历关联用户组
        /// </summary>
        /// <param name="input">角色组Id或名称</param>
        /// <returns></returns>
        [HttpGet("get_usergroup_by_rolegroup")]
        public IActionResult GetUserGroupByRoleGroup([FromQuery] RoleGroupInputDto input)
        {
            var result = _service.GetUserGroupByRoleGroup(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 根据用户Id或名称，遍历关联用户组
        /// </summary>
        /// <param name="input">用户Id或名称</param>
        /// <returns></returns>
        [HttpGet("get_usergroup_by_user")]
        public IActionResult GetUserGroupByUser([FromQuery] UserInfoInputDto input)
        {
            var result = _service.GetUserGroupByUser(input);
            return PageResponse(result);
        }
    }
}