using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using SSS.Api.Seedwork.Controller;
using SSS.Application.Coin.CoinKLineData.Service;
using SSS.Domain.Coin.CoinKLineData.Dto;

namespace SSS.Api.Controllers.Coin
{
    /// <summary>
    /// CoinKLineDataController
    /// </summary> 
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class CoinKLineDataController : ApiBaseController
    {
        private readonly ICoinKLineDataService _service;

        /// <summary>
        /// CoinKLineDataController
        /// </summary>
        /// <param name="service">ICoinKLineDataService</param>
        public CoinKLineDataController(ICoinKLineDataService service)
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
        public IActionResult GetList([FromQuery]CoinKLineDataInputDto input)
        {
            var result = _service.GetListCoinKLineData(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 获取数据
        /// </summary>
        /// <param name="id">id</param>
        /// <returns></returns> 
        [HttpGet("{id}")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetCoinKLineData(string id)
        {
            var result = _service.Get(id);
            return ApiResponse(result);
        }

          /// <summary>
        /// 删除数据
        /// </summary>
        /// <param name="id">id</param>
        /// <returns></returns> 
        [HttpDelete("{id}")]
        [AllowAnonymous]  //匿名访问
        public IActionResult DeleteCoinKLineData(string id)
        {
            var result = _service.Delete(id);
            return DeleteResponse(id, result);
        }

        /// <summary>
        /// AddCoinKLineData
        /// </summary>
        /// <param name="input">CoinKLineDataInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddCoinKLineData([FromBody]CoinKLineDataInputDto input)
        {
            var result = _service.AddCoinKLineData(input);
            return AddResponse(result);
        }
    }
}
