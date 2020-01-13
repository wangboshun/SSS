using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Coin.CoinInfo.Service;
using SSS.Domain.Coin.CoinInfo.Dto;

namespace SSS.Api.Controllers.Coin
{
    /// <summary>
    /// 币币信息
    /// </summary> 
    [ApiVersion("3.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class CoinInfoController : ApiBaseController
    {
        private readonly ICoinInfoService _service;

        /// <summary>
        /// 币币信息
        /// </summary>
        /// <param name="service">ICoinInfoService</param>
        public CoinInfoController(ICoinInfoService service)
        {
            _service = service;
        }

        /// <summary>
        /// 获取所有币币信息列表
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns> 
        [HttpGet("getlist")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetList([FromQuery]CoinInfoInputDto input)
        {
            var result = _service.GetListCoinInfo(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 添加币币
        /// </summary>
        /// <param name="input">CoinInfoInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddCoinInfo([FromBody]CoinInfoInputDto input)
        {
            _service.AddCoinInfo(input);
            return AddResponse(input);
        }
    }
}
