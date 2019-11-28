using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.OperateInfo.Service;
using SSS.Domain.Permission.OperateInfo.Dto;

namespace SSS.Api.Controllers.Permission
{
    /// <summary>
    /// OperateInfoController
    /// </summary> 
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class OperateInfoController : ApiBaseController
    {
        private readonly IOperateInfoService _service;

        /// <summary>
        /// OperateInfoController
        /// </summary>
        /// <param name="service">IOperateInfoService</param>
        public OperateInfoController(IOperateInfoService service)
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
        public IActionResult GetList([FromQuery]OperateInfoInputDto input)
        {
            var result = _service.GetListOperateInfo(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// AddOperateInfo
        /// </summary>
        /// <param name="input">OperateInfoInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddOperateInfo([FromBody]OperateInfoInputDto input)
        {
            _service.AddOperateInfo(input);
            return ApiResponse(input);
        }
    }
}
