using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.Relation.PowerOperateRelation.Service;
using SSS.Domain.Permission.Relation.PowerOperateRelation.Dto;

namespace SSS.Api.Controllers.Permission.Relation
{
    /// <summary>
    /// PowerOperateRelationController
    /// </summary> 
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class PowerOperateRelationController : ApiBaseController
    {
        private readonly IPowerOperateRelationService _service;

        /// <summary>
        /// PowerOperateRelationController
        /// </summary>
        /// <param name="service">IPowerOperateRelationService</param>
        public PowerOperateRelationController(IPowerOperateRelationService service)
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
        public IActionResult GetList([FromQuery]PowerOperateRelationInputDto input)
        {
            var result = _service.GetListPowerOperateRelation(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// AddPowerOperateRelation
        /// </summary>
        /// <param name="input">PowerOperateRelationInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddPowerOperateRelation([FromBody]PowerOperateRelationInputDto input)
        {
            _service.AddPowerOperateRelation(input);
            return ApiResponse(input);
        }
    }
}
