using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using SSS.Api.Seedwork.Controller;
using SSS.Application.System.Job.JobInfo.Service;
using SSS.Domain.System.Job.JobInfo.Dto;

namespace SSS.Api.Controllers.System.Job
{
    /// <summary>
    /// JobInfoController
    /// </summary> 
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class JobInfoController : ApiBaseController
    {
        private readonly IJobInfoService _service;

        /// <summary>
        /// JobInfoController
        /// </summary>
        /// <param name="service">IJobInfoService</param>
        public JobInfoController(IJobInfoService service)
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
        public IActionResult GetList([FromQuery]JobInfoInputDto input)
        {
            var result = _service.GetListJobInfo(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 获取数据
        /// </summary>
        /// <param name="id">id</param>
        /// <returns></returns> 
        [HttpGet("{id}")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetJobInfo(string id)
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
        public IActionResult DeleteJobInfo(string id)
        {
            var result = _service.Delete(id);
            return DeleteResponse(id, result);
        }

        /// <summary>
        /// AddJobInfo
        /// </summary>
        /// <param name="input">JobInfoInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddJobInfo([FromBody]JobInfoInputDto input)
        {
            var result = _service.AddJobInfo(input);
            return AddResponse(result);
        }
    }
}
