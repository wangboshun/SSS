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
    [ApiVersion("3.0")]
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
        /// 获取所有Job错误执行列表
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
        /// <param name="input">JobInfoInputDto</param>
        /// <returns></returns> 
        [HttpGet("getjob")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetJobInfo([FromQuery]JobInfoInputDto input)
        {
            var result = _service.GetJob(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// 添加Job
        /// </summary>
        /// <param name="input">JobInfoInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddJobInfo([FromBody]JobInfoInputDto input)
        {
            var result = _service.AddJob(input);
            return UpdateResponse(input.jobname, result);
        }

        /// <summary>
        /// 删除Job
        /// </summary>
        /// <param name="input">JobInfoInputDto</param>
        /// <returns></returns> 
        [HttpDelete("delete")]
        [AllowAnonymous]  //匿名访问
        public IActionResult DeleteJobInfo([FromBody]JobInfoInputDto input)
        {
            var result = _service.DeleteJob(input);
            return UpdateResponse(input.jobname, result);
        }

        /// <summary>
        /// 暂停Job
        /// </summary>
        /// <param name="input">JobInfoInputDto</param>
        /// <returns></returns> 
        [HttpPost("pause")]
        [AllowAnonymous]  //匿名访问
        public IActionResult PauseJob([FromBody]JobInfoInputDto input)
        {
            var result = _service.PauseJob(input);
            return UpdateResponse(input.jobname, result);
        }

        /// <summary>
        /// 恢复Job
        /// </summary>
        /// <param name="input">JobInfoInputDto</param>
        /// <returns></returns> 
        [HttpPost("resume")]
        [AllowAnonymous]  //匿名访问
        public IActionResult ResumeJob([FromBody]JobInfoInputDto input)
        {
            var result = _service.ResumeJob(input);
            return UpdateResponse(input.jobname, result);
        }

        /// <summary>
        /// 修改Job
        /// </summary>
        /// <param name="input">JobInfoInputDto</param>
        /// <returns></returns> 
        [HttpPost("update")]
        [AllowAnonymous]  //匿名访问
        public IActionResult UpdateJob([FromBody]JobInfoInputDto input)
        {
            var result = _service.UpdateJob(input);
            return UpdateResponse(input.jobname, result);
        }
    }
}
