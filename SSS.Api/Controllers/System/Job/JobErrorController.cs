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
    [Authorize]
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
        /// 获取所有Job错误执行列表
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns> 
        [HttpGet("getlist")]
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
        public IActionResult DeleteJobError(string id)
        {
            var result = _service.Delete(id);
            return DeleteResponse(id, result);
        }

        /// <summary>
        /// 添加错误Job
        /// </summary>
        /// <param name="input">JobErrorInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        public IActionResult AddJobError([FromBody]JobErrorInputDto input)
        {
            var result = _service.AddJobError(input);
            return AddResponse(result);
        }
    }
}
