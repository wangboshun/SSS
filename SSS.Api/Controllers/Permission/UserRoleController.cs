using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.UserRole.Service;
using SSS.Domain.Permission.UserRole.Dto;

namespace SSS.Api.Controllers.Permission
{
    /// <summary>
    /// UserRoleController
    /// </summary> 
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class UserRoleController : ApiBaseController
    {
        private readonly IUserRoleService _service;

        /// <summary>
        /// UserRoleController
        /// </summary>
        /// <param name="service">IUserRoleService</param>
        public UserRoleController(IUserRoleService service)
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
        public IActionResult GetList([FromQuery]UserRoleInputDto input)
        {
            var result = _service.GetListUserRole(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// AddUserRole
        /// </summary>
        /// <param name="input">UserRoleInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddUserRole([FromBody]UserRoleInputDto input)
        {
            _service.AddUserRole(input);
            return ApiResponse(input);
        }
    }
}
