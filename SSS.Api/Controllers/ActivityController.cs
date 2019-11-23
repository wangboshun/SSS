using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Activity.Service;
using SSS.Domain.Activity.Dto;
using SSS.Infrastructure.Repository.Articel;

namespace SSS.Api.Controllers
{
    /// <summary>
    ///     ActivityController
    /// </summary>
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class ActivityController : ApiBaseController
    {
        private readonly IArticelRepository _repository;
        private readonly IActivityService _service;

        /// <summary>
        ///     ActivityController
        /// </summary>
        /// <param name="service">IActivityService</param>
        public ActivityController(IActivityService service, IArticelRepository repository)
        {
            _service = service;
            _repository = repository;
        }

        /// <summary>
        ///     GetList
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns>
        [HttpGet("getlist")]
        [AllowAnonymous] //匿名访问
        public IActionResult GetList([FromQuery] ActivityInputDto input)
        {
            var result = _service.GetListActivity(input);
            return ApiResponse(result);
        }

        /// <summary>
        ///     Get
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns>
        [HttpGet("getbyid")]
        [AllowAnonymous] //匿名访问
        public IActionResult GetById([FromQuery] ActivityInputDto input)
        {
            var result = _service.GetById(input);
            return ApiResponse(result);
        }

        /// <summary>
        ///     AddActivity
        /// </summary>
        /// <param name="input">ActivityInputDto</param>
        /// <returns></returns>
        [HttpPost("add")]
        [AllowAnonymous] //匿名访问
        public IActionResult AddActivity([FromBody] ActivityInputDto input)
        {
            _service.AddActivity(input);
            return ApiResponse(input);
        }
    }
}