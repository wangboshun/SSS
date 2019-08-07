using Hangfire;
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
    [Authorize]
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class TradeController : ApiBaseController
    {
        private readonly ITradeService _trade;

        public TradeController(ITradeService trade)
        {
            _trade = trade;
        }

        /// <summary>
        /// GetList
        /// </summary>
        /// <param name="trade">TradeInputDto</param>
        /// <returns></returns> 
        [HttpGet("getlist")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetList([FromQuery]TradeInputDto input)
        {
            input.userid = UserInfo.id;
            input.pageindex = 0;
            input.pagesize = 10;
            var result = _trade.GetListTrade(input);
            return Response(result);
        }

        [HttpGet("operatrade")]
        [AllowAnonymous]  //匿名访问
        public IActionResult OperaTrade([FromQuery]TradeInputDto input)
        {
            RecurringJob.AddOrUpdate(() => _trade.OperateTrade(input), Cron.MinuteInterval(1));
            return Response(input);
        }
    }
}