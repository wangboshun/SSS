using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.RoleMenu.Service;
using SSS.Domain.Permission.RoleMenu.Dto;

namespace SSS.Api.Controllers.Permission
{
    /// <summary>
    /// RoleMenuController
    /// </summary> 
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class RoleMenuController : ApiBaseController
    {
        private readonly IRoleMenuService _service;

        /// <summary>
        /// RoleMenuController
        /// </summary>
        /// <param name="service">IRoleMenuService</param>
        public RoleMenuController(IRoleMenuService service)
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
        public IActionResult GetList([FromQuery]RoleMenuInputDto input)
        {
            var result = _service.GetListRoleMenu(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// AddRoleMenu
        /// </summary>
        /// <param name="input">RoleMenuInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddRoleMenu([FromBody]RoleMenuInputDto input)
        {
            _service.AddRoleMenu(input);
            return ApiResponse(input);
        }
    }
}
