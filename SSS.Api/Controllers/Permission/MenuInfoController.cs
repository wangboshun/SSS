using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.MenuInfo.Service;
using SSS.Domain.Permission.MenuInfo.Dto;

namespace SSS.Api.Controllers.Permission
{
    /// <summary>
    /// MenuInfoController
    /// </summary> 
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class MenuInfoController : ApiBaseController
    {
        private readonly IMenuInfoService _service;

        /// <summary>
        /// MenuInfoController
        /// </summary>
        /// <param name="service">IMenuInfoService</param>
        public MenuInfoController(IMenuInfoService service)
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
        public IActionResult GetList([FromQuery]MenuInfoInputDto input)
        {
            var result = _service.GetListMenuInfo(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// AddMenuInfo
        /// </summary>
        /// <param name="input">MenuInfoInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddMenuInfo([FromBody]MenuInfoInputDto input)
        {
            _service.AddMenuInfo(input);
            return ApiResponse(input);
        }
    }
}
