using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.Relation.PowerGroupOperateRelation.Service;
using SSS.Domain.Permission.Relation.PowerGroupOperateRelation.Dto;

namespace SSS.Api.Controllers.Permission.Relation
{
    /// <summary>
    /// PowerGroupOperateRelationController
    /// </summary> 
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class PowerGroupOperateRelationController : ApiBaseController
    {
        private readonly IPowerGroupOperateRelationService _service;

        /// <summary>
        /// PowerGroupOperateRelationController
        /// </summary>
        /// <param name="service">IPowerGroupOperateRelationService</param>
        public PowerGroupOperateRelationController(IPowerGroupOperateRelationService service)
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
        public IActionResult GetList([FromQuery]PowerGroupOperateRelationInputDto input)
        {
            var result = _service.GetListPowerGroupOperateRelation(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// AddPowerGroupOperateRelation
        /// </summary>
        /// <param name="input">PowerGroupOperateRelationInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddPowerGroupOperateRelation([FromBody]PowerGroupOperateRelationInputDto input)
        {
            _service.AddPowerGroupOperateRelation(input);
            return ApiResponse(input);
        }
    }
}
