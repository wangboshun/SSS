using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Coin.CoinMessage.Service;
using SSS.Domain.Coin.CoinMessage.Dto;

namespace SSS.Api.Controllers.Coin
{
    /// <summary>
    /// 利好消息
    /// </summary>
    [ApiVersion("3.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    [Authorize]
    public class CoinMessageController : ApiBaseController
    {
        private readonly ICoinMessageService _service;

        /// <summary>
        /// 利好消息
        /// </summary>
        /// <param name="service">ICoinMessageService</param>
        public CoinMessageController(ICoinMessageService service)
        {
            _service = service;
        }

        /// <summary>
        /// 添加利好消息
        /// </summary>
        /// <param name="input">CoinMessageInputDto</param>
        /// <returns></returns>
        [HttpPost("add")]
        public IActionResult AddCoinMessage([FromBody] CoinMessageInputDto input)
        {
            _service.AddCoinMessage(input);
            return AddResponse(input);
        }

        /// <summary>
        /// 获取所有信息列表
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns>
        [HttpGet("getlist")]
        public IActionResult GetList([FromQuery] CoinMessageInputDto input)
        {
            var result = _service.GetListCoinMessage(input);
            return PageResponse(result);
        }
    }
}