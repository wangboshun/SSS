using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.Info.PowerInfo.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Domain.Permission.Info.PowerInfo.Dto;
using SSS.Domain.Permission.Info.UserInfo.Dto;

namespace SSS.Api.Controllers.Permission.Info
{
    /// <summary>
    /// 权限信息
    /// </summary> 
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    [Authorize]
    public class PowerInfoController : ApiBaseController
    {
        private readonly IPowerInfoService _service;

        /// <summary>
        /// 权限信息
        /// </summary>
        /// <param name="service">IPowerInfoService</param>
        public PowerInfoController(IPowerInfoService service)
        {
            _service = service;
        }

        /// <summary>
        /// 获取所有权限信息
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns> 
        [HttpGet("getlist")]
        public IActionResult GetList([FromQuery]PowerInfoInputDto input)
        {
            var result = _service.GetListPowerInfo(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 添加权限信息
        /// </summary>
        /// <param name="input">PowerInfoInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        public IActionResult AddPowerInfo([FromBody]PowerInfoInputDto input)
        {
            var result = _service.AddPowerInfo(input);
            return AddResponse(result);
        }

        /// <summary>
        /// 删除权限信息
        /// </summary>
        /// <param name="id">id</param>
        /// <returns></returns> 
        [HttpDelete("{id}")]
        public IActionResult DeletePowerInfo(string id)
        {
            var result = _service.DeletePowerInfo(id);
            return DeleteResponse(id, result);
        }

        /// <summary>
        ///     获取权限的所有下级
        /// </summary>
        /// <param name="powerid">权限Id</param>
        /// <returns></returns>
        [HttpGet("get_children_by_powerid")]
        public IActionResult GetChildren(string powerid)
        {
            var result = _service.GetChildren(powerid);
            return ApiResponse(result);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联权限
        /// </summary>
        /// <param name="input">权限组Id或名称</param>
        /// <returns></returns> 
        [HttpGet("get_power_by_powergroup")]
        public IActionResult GetPowerByPowerGroup([FromQuery]PowerGroupInputDto input)
        {
            var result = _service.GetPowerByPowerGroup(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 根据角色组Id或名称，遍历关联权限
        /// </summary>
        /// <param name="input">角色组Id或名称</param>
        /// <returns></returns> 
        [HttpGet("get_power_by_rolegroup")]
        public IActionResult GetPowerByRoleGroup([FromQuery]RoleGroupInputDto input)
        {
            var result = _service.GetPowerByRoleGroup(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 根据用户组Id或名称，遍历关联权限
        /// </summary>
        /// <param name="input">用户组Id或名称</param>
        /// <returns></returns> 
        [HttpGet("get_power_by_usergroup")]
        public IActionResult GetPowerByUserGroup([FromQuery]UserGroupInputDto input)
        {
            var result = _service.GetPowerByUserGroup(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 根据用户Id或名称，遍历关联权限
        /// </summary>
        /// <param name="input">用户Id或名称</param>
        /// <returns></returns> 
        [HttpGet("get_power_by_user")]
        public IActionResult GetPowerByUser([FromQuery]UserInfoInputDto input)
        {
            var result = _service.GetPowerByUser(input);
            return PageResponse(result);
        }
    }
}
