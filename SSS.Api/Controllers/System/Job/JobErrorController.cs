using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.System.Job.JobError.Service;
using SSS.Domain.System.Job.JobError.Dto;

namespace SSS.Api.Controllers.System.Job
{
    /// <summary>
    /// JobErrorController
    /// </summary> 
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class JobErrorController : ApiBaseController
    {
        private readonly IJobErrorService _service;

        /// <summary>
        /// JobErrorController
        /// </summary>
        /// <param name="service">IJobErrorService</param>
        public JobErrorController(IJobErrorService service)
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
        public IActionResult GetList([FromQuery]JobErrorInputDto input)
        {
            var result = _service.GetListJobError(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 获取数据
        /// </summary>
        /// <param name="id">id</param>
        /// <returns></returns> 
        [HttpGet("{id}")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetJobError(string id)
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
        public IActionResult DeleteJobError(string id)
        {
            var result = _service.Delete(id);
            return DeleteResponse(id, result);
        }

        /// <summary>
        /// AddJobError
        /// </summary>
        /// <param name="input">JobErrorInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddJobError([FromBody]JobErrorInputDto input)
        {
            var result = _service.AddJobError(input);
            return AddResponse(result);
        }
    }
}
