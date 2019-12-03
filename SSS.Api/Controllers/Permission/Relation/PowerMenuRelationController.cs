using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.Relation.PowerMenuRelation.Service;
using SSS.Domain.Permission.Relation.PowerMenuRelation.Dto;

namespace SSS.Api.Controllers.Permission.Relation
{
    /// <summary>
    /// PowerMenuRelationController
    /// </summary> 
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class PowerMenuRelationController : ApiBaseController
    {
        private readonly IPowerMenuRelationService _service;

        /// <summary>
        /// PowerMenuRelationController
        /// </summary>
        /// <param name="service">IPowerMenuRelationService</param>
        public PowerMenuRelationController(IPowerMenuRelationService service)
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
        public IActionResult GetList([FromQuery]PowerMenuRelationInputDto input)
        {
            var result = _service.GetListPowerMenuRelation(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// AddPowerMenuRelation
        /// </summary>
        /// <param name="input">PowerMenuRelationInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddPowerMenuRelation([FromBody]PowerMenuRelationInputDto input)
        {
            _service.AddPowerMenuRelation(input);
            return ApiResponse(input);
        }
    }
}
