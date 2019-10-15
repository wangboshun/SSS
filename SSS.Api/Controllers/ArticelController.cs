using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using SSS.Api.Seedwork.Controller;
using SSS.Application.Articel.Service;
using SSS.Domain.Articel.Dto;

namespace SSS.Api.Controllers
{
    /// <summary>
    ///     ArticelController
    /// </summary>
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class ArticelController : ApiBaseController
    {
        private readonly IArticelService _service;

        /// <summary>
        ///     ArticelController
        /// </summary>
        /// <param name="service">IArticelService</param>
        public ArticelController(IArticelService service)
        {
            _service = service;
        }

        /// <summary>
        ///     GetList
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns>
        [HttpGet("getlist")]
        [AllowAnonymous] //匿名访问
        public IActionResult GetList([FromQuery] ArticelInputDto input)
        {
            var result = _service.GetListArticel(input);
            return Response(result);
        }

        /// <summary>
        ///     AddArticel
        /// </summary>
        /// <param name="input">ArticelInputDto</param>
        /// <returns></returns>
        [HttpPost("add")]
        [AllowAnonymous] //匿名访问
        public IActionResult AddArticel([FromBody] ArticelInputDto input)
        {
            _service.AddArticel(input);
            return Response(input);
        }
    }
}