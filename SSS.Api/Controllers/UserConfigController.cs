using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using SSS.Api.Seedwork;
using SSS.Application.UserConfig.Service;
using SSS.Domain.UserConfig.Dto;

namespace SSS.Api.Controllers
{
    /// <summary>
    /// UserConfigController
    /// </summary>
    [Authorize]
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class UserConfigController : ApiBaseController
    {
        private readonly IUserConfigService _service;

        /// <summary>
        /// UserConfigController
        /// </summary>
        /// <param name="service">IUserConfigService</param>
        public UserConfigController(IUserConfigService service)
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
        public IActionResult GetList([FromQuery]UserConfigInputDto input)
        {
            var result = _service.GetListUserConfig(input);
            return Response(result);
        }

        /// <summary>
        /// AddUserConfig
        /// </summary>
        /// <param name="input">UserConfigInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddUserConfig([FromBody]UserConfigInputDto input)
        {
            _service.AddUserConfig(input);
            return Response(input);
        }
    }
}
