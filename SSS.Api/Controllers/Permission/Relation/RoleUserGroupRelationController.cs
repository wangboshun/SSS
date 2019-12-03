using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.Relation.RoleUserGroupRelation.Service;
using SSS.Domain.Permission.Relation.RoleUserGroupRelation.Dto;

namespace SSS.Api.Controllers.Permission.Relation
{
    /// <summary>
    /// RoleUserGroupRelationController
    /// </summary> 
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class RoleUserGroupRelationController : ApiBaseController
    {
        private readonly IRoleUserGroupRelationService _service;

        /// <summary>
        /// RoleUserGroupRelationController
        /// </summary>
        /// <param name="service">IRoleUserGroupRelationService</param>
        public RoleUserGroupRelationController(IRoleUserGroupRelationService service)
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
        public IActionResult GetList([FromQuery]RoleUserGroupRelationInputDto input)
        {
            var result = _service.GetListRoleUserGroupRelation(input);
            return ApiResponse(result);
        } 

        /// <summary>
        /// AddRoleUserGroupRelation
        /// </summary>
        /// <param name="input">RoleUserGroupRelationInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddRoleUserGroupRelation([FromBody]RoleUserGroupRelationInputDto input)
        {
            _service.AddRoleUserGroupRelation(input);
            return ApiResponse(input);
        }
    }
}
