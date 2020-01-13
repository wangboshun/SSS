using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Coin.CoinArticel.Service;
using SSS.Domain.Coin.CoinArticel.Dto;

namespace SSS.Api.Controllers.Coin
{
    /// <summary>
    /// 币币新闻
    /// </summary>
    [ApiVersion("3.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    [Authorize]
    public class CoinArticelController : ApiBaseController
    {
        private readonly ICoinArticelService _service;

        /// <summary>
        ///  币币新闻
        /// </summary>
        /// <param name="service">ICoinArticelService</param>
        public CoinArticelController(ICoinArticelService service)
        {
            _service = service;
        }

        /// <summary>
        ///    获取所有信息列表
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns>
        [HttpGet("getlist")] 
        public IActionResult GetList([FromQuery] CoinArticelInputDto input)
        {
            var result = _service.GetListCoinArticel(input);
            return PageResponse(result);
        }

        /// <summary>
        ///     快讯
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns>
        [HttpGet("getnews")] 
        public IActionResult GetNews([FromQuery] CoinArticelInputDto input)
        {
            var result = _service.GetNews(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// 新闻详情
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpGet("getnewsdetail")] 
        public IActionResult GetNewsDetail(string id)
        {
            var result = _service.Get(id);
            return ApiResponse(result);
        }

        /// <summary>
        ///  添加新闻
        /// </summary>
        /// <param name="input">CoinArticelInputDto</param>
        /// <returns></returns>
        [HttpPost("add")] 
        public IActionResult AddCoinArticel([FromBody] CoinArticelInputDto input)
        {
            _service.AddCoinArticel(input);
            return AddResponse(input);

        }
    }
}