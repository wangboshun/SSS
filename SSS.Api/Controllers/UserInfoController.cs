using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using SSS.Api.Seedwork;
using SSS.Application.UserInfo.Service;
using SSS.Domain.UserInfo.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Api.Controllers
{
    /// <summary>
    /// UserInfoController
    /// </summary>
    [Authorize]
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class UserInfoController : ApiBaseController
    {
        [Autowired]
        private readonly IUserInfoService _service;

        /// <summary>
        /// AddUserInfo
        /// </summary>
        /// <param name="input">UserInfoInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddUserInfo([FromBody]UserInfoInputDto input)
        {
            _service.AddUserInfo(input);
            return Response(input);
        } 

        /// <summary>
        /// ListUserInfo
        /// </summary>
        /// <param name="input">UserInfoInputDto</param>
        /// <returns></returns> 
        [HttpGet("list")]
        [AllowAnonymous]  //匿名访问
        public IActionResult ListUser([FromQuery]UserInfoInputDto input)
        {
            object result = _service.GetListUser(input);
            return Response(result);
        }

        /// <summary>
        /// getbyphone
        /// </summary>
        /// <param name="input">UserInfoInputDto</param>
        /// <returns></returns> 
        [HttpGet("getbyphone")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetByPhone([FromQuery]UserInfoInputDto input)
        {
            object result = _service.GetByPhone(input);
            return Response(result);
        }
    }
}
