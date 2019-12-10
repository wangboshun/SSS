using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.Info.PowerInfo.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Info.PowerInfo.Dto;

namespace SSS.Api.Controllers.Permission.Info
{
    /// <summary>
    /// PowerInfoController
    /// </summary> 
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class PowerInfoController : ApiBaseController
    {
        private readonly IPowerInfoService _service;

        /// <summary>
        /// PowerInfoController
        /// </summary>
        /// <param name="service">IPowerInfoService</param>
        public PowerInfoController(IPowerInfoService service)
        {
            _service = service;
        }

        /// <summary>
        /// GetList
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns> 
        [HttpGet("getlist")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetList([FromQuery]PowerInfoInputDto input)
        {
            var result = _service.GetListPowerInfo(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// 添加权限信息
        /// </summary>
        /// <param name="input">PowerInfoInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddPowerInfo([FromBody]PowerInfoInputDto input)
        {
            var result = _service.AddPowerInfo(input);
            return ApiResponse(result, result, result ? "增加成功" : "增加失败");
        }

        /// <summary>
        /// 删除权限信息
        /// </summary>
        /// <param name="id">id</param>
        /// <returns></returns> 
        [HttpDelete("{id}")]
        [AllowAnonymous]  //匿名访问
        public IActionResult DeletePowerInfo(string id)
        {
            var result = _service.DeletePowerInfo(id);
            return ApiResponse(result, result, result ? "删除成功" : "删除失败");
        }

        /// <summary>
        ///     获取权限的所有下级
        /// </summary>
        /// <param name="powerid">权限Id</param>
        /// <returns></returns>
        [HttpGet("get_children_by_powerid")]
        [AllowAnonymous] //匿名访问
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
        [AllowAnonymous]  //匿名访问
        public IActionResult GetPowerByPowerGroup([FromQuery]PowerGroupInputDto input)
        {
            var result = _service.GetPowerByPowerGroup(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// 根据角色组Id或名称，遍历关联权限
        /// </summary>
        /// <param name="input">角色组Id或名称</param>
        /// <returns></returns> 
        [HttpGet("get_power_by_rolegroup")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetPowerByRoleGroup([FromQuery]RoleGroupInputDto input)
        {
            var result = _service.GetPowerByRoleGroup(input);
            return ApiResponse(result);
        }
    }
}
