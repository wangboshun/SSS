using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.RoleOperate.Service;
using SSS.Domain.Permission.RoleOperate.Dto;

namespace SSS.Api.Controllers.Permission
{
    /// <summary>
    /// RoleOperateController
    /// </summary> 
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class RoleOperateController : ApiBaseController
    {
        private readonly IRoleOperateService _service;

        /// <summary>
        /// RoleOperateController
        /// </summary>
        /// <param name="service">IRoleOperateService</param>
        public RoleOperateController(IRoleOperateService service)
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
        public IActionResult GetList([FromQuery]RoleOperateInputDto input)
        {
            var result = _service.GetListRoleOperate(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// GetOperateByRole
        /// </summary>
        /// <param name="roleid">roleid</param>
        /// <returns></returns> 
        [HttpGet("getoperatebyrole")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetOperateByRole(string roleid)
        {
            var result = _service.GetOperateByRole(roleid);
            return ApiResponse(result);
        }

        /// <summary>
        /// AddRoleOperate
        /// </summary>
        /// <param name="input">RoleOperateInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddRoleOperate([FromBody]RoleOperateInputDto input)
        {
            _service.AddRoleOperate(input);
            return ApiResponse(input);
        }
    }
}
