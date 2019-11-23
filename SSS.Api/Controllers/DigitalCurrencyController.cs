using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.DigitalCurrency.Service;
using SSS.Domain.DigitalCurrency.Dto;

namespace SSS.Api.Controllers
{
    /// <summary>
    /// DigitalCurrencyController
    /// </summary> 
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class DigitalCurrencyController : ApiBaseController
    {
        private readonly IDigitalCurrencyService _service;

        /// <summary>
        /// DigitalCurrencyController
        /// </summary>
        /// <param name="service">IDigitalCurrencyService</param>
        public DigitalCurrencyController(IDigitalCurrencyService service)
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
        public IActionResult GetList([FromQuery]DigitalCurrencyInputDto input)
        {
            var result = _service.GetListDigitalCurrency(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// AddDigitalCurrency
        /// </summary>
        /// <param name="input">DigitalCurrencyInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddDigitalCurrency([FromBody]DigitalCurrencyInputDto input)
        {
            _service.AddDigitalCurrency(input);
            return ApiResponse(input);
        }
    }
}
