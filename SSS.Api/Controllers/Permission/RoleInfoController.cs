using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.RoleInfo.Service;
using SSS.Domain.Permission.RoleInfo.Dto;

namespace SSS.Api.Controllers.Permission
{
    /// <summary>
    /// RoleInfoController
    /// </summary> 
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class RoleInfoController : ApiBaseController
    {
        private readonly IRoleInfoService _service;

        /// <summary>
        /// RoleInfoController
        /// </summary>
        /// <param name="service">IRoleInfoService</param>
        public RoleInfoController(IRoleInfoService service)
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
        public IActionResult GetList([FromQuery]RoleInfoInputDto input)
        {
            var result = _service.GetListRoleInfo(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// AddRoleInfo
        /// </summary>
        /// <param name="input">RoleInfoInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddRoleInfo([FromBody]RoleInfoInputDto input)
        {
            _service.AddRoleInfo(input);
            return ApiResponse(input);
        }
    }
}
