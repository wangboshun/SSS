using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Coin.CoinTrade.Service;
using SSS.Domain.Coin.CoinTrade.Dto;

namespace SSS.Api.Controllers.Coin
{
    /// <summary>
    /// 币币账单
    /// </summary>
    [ApiVersion("3.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    [Authorize]
    public class CoinTradeController : ApiBaseController
    {
        private readonly ICoinTradeService _service;

        /// <summary>
        /// 币币账单
        /// </summary>
        /// <param name="service">ICoinTradeService</param>
        public CoinTradeController(ICoinTradeService service)
        {
            _service = service;
        }

        /// <summary>
        /// 添加账单
        /// </summary>
        /// <param name="input">CoinTradeInputDto</param>
        /// <returns></returns>
        [HttpPost("add")]
        public IActionResult AddCoinTrade([FromBody] CoinTradeInputDto input)
        {
            _service.AddCoinTrade(input);
            return AddResponse(input);
        }

        /// <summary>
        /// 获取所有账单列表
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns>
        [HttpGet("getlist")]
        public IActionResult GetList([FromQuery] CoinTradeInputDto input)
        {
            var result = _service.GetListCoinTrade(input);
            return PageResponse(result);
        }
    }
}