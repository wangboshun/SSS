using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.Relation.PowerGroupMenuRelation.Service;
using SSS.Domain.Permission.Relation.PowerGroupMenuRelation.Dto;

namespace SSS.Api.Controllers.Permission.Relation
{
    /// <summary>
    /// PowerGroupMenuRelationController
    /// </summary> 
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class PowerGroupMenuRelationController : ApiBaseController
    {
        private readonly IPowerGroupMenuRelationService _service;

        /// <summary>
        /// PowerGroupMenuRelationController
        /// </summary>
        /// <param name="service">IPowerGroupMenuRelationService</param>
        public PowerGroupMenuRelationController(IPowerGroupMenuRelationService service)
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
        public IActionResult GetList([FromQuery]PowerGroupMenuRelationInputDto input)
        {
            var result = _service.GetListPowerGroupMenuRelation(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// AddPowerGroupMenuRelation
        /// </summary>
        /// <param name="input">PowerGroupMenuRelationInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddPowerGroupMenuRelation([FromBody]PowerGroupMenuRelationInputDto input)
        {
            _service.AddPowerGroupMenuRelation(input);
            return ApiResponse(input);
        }
    }
}
