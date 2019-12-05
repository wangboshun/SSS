using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.Group.UserGroup.Service;
using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Domain.Permission.Relation.UserGroupPowerGroupRelation.Dto;
using SSS.Domain.Permission.Relation.UserGroupRelation.Dto;

namespace SSS.Api.Controllers.Permission.Group
{
    /// <summary>
    /// UserGroupController
    /// </summary> 
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
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
        /// 获取所有用户组
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns> 
        [HttpGet("getlist")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetList([FromQuery]UserGroupInputDto input)
        {
            var result = _service.GetListUserGroup(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// 增加用户组
        /// </summary>
        /// <param name="input">用户组名称</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddUserGroup([FromBody]UserGroupInputDto input)
        {
            _service.AddUserGroup(input);
            return ApiResponse(input);
        }

        /// <summary>
        /// 根据用户Id或名称，遍历关联用户组
        /// </summary>
        /// <param name="input">用户Id或名称</param>
        /// <returns></returns> 
        [HttpGet("get_usergroup_by_user")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetUserGroupByUser([FromQuery]UserGroupRelationInputDto input)
        {
            var result = _service.GetUserGroupByUser(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联用户组
        /// </summary>
        /// <param name="input">权限组Id或名称</param>
        /// <returns></returns> 
        [HttpGet("get_usergroup_by_powergroup")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetUserGroupByPowerGroup([FromQuery]UserGroupPowerGroupRelationInputDto input)
        {
            var result = _service.GetUserGroupByPowerGroup(input);
            return ApiResponse(result);
        }
    }
}
