using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using SSS.Api.Seedwork.Controller;
using SSS.Application.UserApi.Service;
using SSS.Domain.UserApi.Dto;

namespace SSS.Api.Controllers
{
    /// <summary>
    /// UserApiController
    /// </summary>
    [Authorize]
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class UserApiController : ApiBaseController
    {
        private readonly IUserApiService _service;

        /// <summary>
        /// UserApiController
        /// </summary>
        /// <param name="service">IUserApiService</param>
        public UserApiController(IUserApiService service)
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
        public IActionResult GetList([FromQuery]UserApiInputDto input)
        {
            var result = _service.GetListUserApi(input);
            return Response(result);
        }

        [HttpGet("getbyuserid")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetByUserId([FromQuery]UserApiInputDto input)
        {
            input.UserId = UserInfo.id;
            var result = _service.GetByUserId(input);
            return Response(result);
        }

        /// <summary>
        /// AddUserApi
        /// </summary>
        /// <param name="input">UserApiInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddUserApi([FromBody]UserApiInputDto input)
        {
            input.UserId = UserInfo.id;
            _service.AddUserApi(input);
            return Response(input);
        } 

        /// <summary>
        /// UpdateUserApi
        /// </summary>
        /// <param name="input">UserApiInputDto</param>
        /// <returns></returns> 
        [HttpPost("update")]
        [AllowAnonymous]  //匿名访问
        public IActionResult UpdateUserApi([FromBody]UserApiInputDto input)
        {
            input.UserId = UserInfo.id;
            _service.UpdateUserApi(input);
            return Response(input);
        }
    }
}
