using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.CoinMessage.Service;
using SSS.Domain.CoinMessage.Dto;

namespace SSS.Api.Controllers
{
    /// <summary>
    /// CoinMessageController
    /// </summary> 
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class CoinMessageController : ApiBaseController
    {
        private readonly ICoinMessageService _service;

        /// <summary>
        /// CoinMessageController
        /// </summary>
        /// <param name="service">ICoinMessageService</param>
        public CoinMessageController(ICoinMessageService service)
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
        public IActionResult GetList([FromQuery]CoinMessageInputDto input)
        {
            var result = _service.GetListCoinMessage(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// AddCoinMessage
        /// </summary>
        /// <param name="input">CoinMessageInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddCoinMessage([FromBody]CoinMessageInputDto input)
        {
            _service.AddCoinMessage(input);
            return ApiResponse(input);
        }
    }
}
