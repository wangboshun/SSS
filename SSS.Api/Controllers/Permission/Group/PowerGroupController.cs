using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.Group.PowerGroup.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Relation.PowerGroupMenuRelation.Dto;
using SSS.Domain.Permission.Relation.PowerGroupOperateRelation.Dto;
using SSS.Domain.Permission.Relation.PowerGroupRelation.Dto;
using SSS.Domain.Permission.Relation.UserGroupPowerGroupRelation.Dto;
using SSS.Domain.Permission.Relation.UserPowerGroupRelation.Dto;

namespace SSS.Api.Controllers.Permission.Group
{
    /// <summary>
    /// PowerGroupController
    /// </summary> 
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class PowerGroupController : ApiBaseController
    {
        private readonly IPowerGroupService _service;

        /// <summary>
        /// 权限组
        /// </summary>
        /// <param name="service">IPowerGroupService</param>
        public PowerGroupController(IPowerGroupService service)
        {
            _service = service;
        }

        /// <summary>
        /// 获取所有权限组
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns> 
        [HttpGet("getlist")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetList([FromQuery]PowerGroupInputDto input)
        {
            var result = _service.GetListPowerGroup(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// 增加权限组
        /// </summary>
        /// <param name="input">权限组名称</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddPowerGroup([FromBody]PowerGroupInputDto input)
        {
            _service.AddPowerGroup(input);
            return ApiResponse(input);
        }

        /// <summary>
        /// 根据权限Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input">权限Id或名称</param>
        /// <returns></returns> 
        [HttpGet("get_powergroup_by_power")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetUserGroupByUser([FromQuery]PowerGroupRelationInputDto input)
        {
            var result = _service.GetPowerGroupByPower(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// 根据菜单Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input">菜单Id或名称</param>
        /// <returns></returns> 
        [HttpGet("get_powergroup_by_menu")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetPowerGroupByMenu([FromQuery]PowerGroupMenuRelationInputDto input)
        {
            var result = _service.GetPowerGroupByMenu(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// 根据操作Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input">操作Id或名称</param>
        /// <returns></returns> 
        [HttpGet("get_powergroup_by_operate")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetPowerGroupByOperate([FromQuery]PowerGroupOperateRelationInputDto input)
        {
            var result = _service.GetPowerGroupByOperate(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// 根据用户Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input">用户Id或名称</param>
        /// <returns></returns> 
        [HttpGet("get_powergroup_by_user")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetPowerGroupByUser([FromQuery]UserPowerGroupRelationInputDto input)
        {
            var result = _service.GetPowerGroupByUser(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// 根据用户组Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input">用户组Id或名称</param>
        /// <returns></returns> 
        [HttpGet("get_powergroup_by_usergroup")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetPowerGroupByUserGroup([FromQuery]UserGroupPowerGroupRelationInputDto input)
        {
            var result = _service.GetPowerGroupByUserGroup(input);
            return ApiResponse(result);
        }
    }
}
