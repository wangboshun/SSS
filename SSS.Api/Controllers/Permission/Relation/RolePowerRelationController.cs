using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.Relation.RolePowerRelation.Service;
using SSS.Domain.Permission.Relation.RolePowerRelation.Dto;

namespace SSS.Api.Controllers.Permission.Relation
{
    /// <summary>
    /// RolePowerRelationController
    /// </summary> 
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class RolePowerRelationController : ApiBaseController
    {
        private readonly IRolePowerRelationService _service;

        /// <summary>
        /// RolePowerRelationController
        /// </summary>
        /// <param name="service">IRolePowerRelationService</param>
        public RolePowerRelationController(IRolePowerRelationService service)
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
        public IActionResult GetList([FromQuery]RolePowerRelationInputDto input)
        {
            var result = _service.GetListRolePowerRelation(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// AddRolePowerRelation
        /// </summary>
        /// <param name="input">RolePowerRelationInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddRolePowerRelation([FromBody]RolePowerRelationInputDto input)
        {
            _service.AddRolePowerRelation(input);
            return ApiResponse(input);
        }
    }
}
