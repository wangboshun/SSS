using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Trade.Service;
using SSS.Domain.Trade.Dto;

namespace SSS.Api.Controllers
{
    /// <summary>
    /// TradeController
    /// </summary> 
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class TradeController : ApiBaseController
    {
        private readonly ITradeService _service;

        /// <summary>
        /// TradeController
        /// </summary>
        /// <param name="service">ITradeService</param>
        public TradeController(ITradeService service)
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
        public IActionResult GetList([FromQuery]TradeInputDto input)
        {
            var result = _service.GetListTrade(input);
            return Response(result);
        }

        /// <summary>
        /// AddTrade
        /// </summary>
        /// <param name="input">TradeInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddTrade([FromBody]TradeInputDto input)
        {
            _service.AddTrade(input);
            return Response(input);
        }
    }
}
