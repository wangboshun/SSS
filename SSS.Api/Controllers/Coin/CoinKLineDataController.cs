using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Coin.CoinKLineData.Service;
using SSS.Domain.Coin.CoinKLineData.Dto;

namespace SSS.Api.Controllers.Coin
{
    /// <summary>
    /// K线数据详情
    /// </summary>
    [ApiVersion("3.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    [Authorize]
    public class CoinKLineDataController : ApiBaseController
    {
        private readonly ICoinKLineDataService _service;

        /// <summary>
        /// K线数据详情
        /// </summary>
        /// <param name="service">ICoinKLineDataService</param>
        public CoinKLineDataController(ICoinKLineDataService service)
        {
            _service = service;
        }

        /// <summary>
        /// 添加K线数据
        /// </summary>
        /// <param name="input">CoinKLineDataInputDto</param>
        /// <returns></returns>
        [HttpPost("add")]
        public IActionResult AddCoinKLineData([FromBody] CoinKLineDataInputDto input)
        {
            var result = _service.AddCoinKLineData(input);
            return AddResponse(result);
        }

        /// <summary>
        /// 删除数据
        /// </summary>
        /// <param name="id">id</param>
        /// <returns></returns>
        [HttpDelete("{id}")]
        public IActionResult DeleteCoinKLineData(string id)
        {
            var result = _service.Delete(id);
            return DeleteResponse(id, result);
        }

        /// <summary>
        /// 获取数据
        /// </summary>
        /// <param name="id">id</param>
        /// <returns></returns>
        [HttpGet("{id}")]
        public IActionResult GetCoinKLineData(string id)
        {
            var result = _service.Get(id);
            return ApiResponse(result);
        }

        /// <summary>
        /// 获取所有K线数据列表
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns>
        [HttpGet("getlist")]
        public IActionResult GetList([FromQuery] CoinKLineDataInputDto input)
        {
            var result = _service.GetListCoinKLineData(input);
            return PageResponse(result);
        }
    }
}