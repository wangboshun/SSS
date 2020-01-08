using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Coin.CoinAnalyse.Service;
using SSS.Domain.Coin.CoinAnalyse.Dto;

namespace SSS.Api.Controllers.Coin
{
    /// <summary>
    /// 币币分析
    /// </summary> 
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class CoinAnalyseController : ApiBaseController
    {
        private readonly ICoinAnalyseService _service;

        /// <summary>
        /// 币币分析
        /// </summary>
        /// <param name="service">ICoinAnalyseService</param>
        public CoinAnalyseController(ICoinAnalyseService service)
        {
            _service = service;
        }

        /// <summary>
        /// 获取所有分析列表
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns> 
        [HttpGet("getlist")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetList([FromQuery]CoinAnalyseInputDto input)
        {
            var result = _service.GetListCoinAnalyse(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 添加
        /// </summary>
        /// <param name="input">CoinAnalyseInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddCoinAnalyse([FromBody]CoinAnalyseInputDto input)
        {
            _service.AddCoinAnalyse(input);
            return AddResponse(input);
        }
    }
}
