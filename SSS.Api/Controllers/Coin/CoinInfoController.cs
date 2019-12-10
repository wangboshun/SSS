using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Coin.CoinInfo.Service;
using SSS.Domain.Coin.CoinInfo.Dto;

namespace SSS.Api.Controllers.Coin
{
    /// <summary>
    /// CoinInfoController
    /// </summary> 
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class CoinInfoController : ApiBaseController
    {
        private readonly ICoinInfoService _service;

        /// <summary>
        /// CoinInfoController
        /// </summary>
        /// <param name="service">ICoinInfoService</param>
        public CoinInfoController(ICoinInfoService service)
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
        public IActionResult GetList([FromQuery]CoinInfoInputDto input)
        {
            var result = _service.GetListCoinInfo(input);
            return PageResponse(result);
        }

        /// <summary>
        /// AddCoinInfo
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
