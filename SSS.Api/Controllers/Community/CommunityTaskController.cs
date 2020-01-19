using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Community.CommunityTask.Service;
using SSS.Domain.Community.CommunityTask.Dto;

namespace SSS.Api.Controllers.Community
{
    /// <summary>
    /// 任务发布
    /// </summary>
    [ApiVersion("4.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    [Authorize]
    public class CommunityTaskController : ApiBaseController
    {
        private readonly ICommunityTaskService _service;

        /// <summary>
        /// 任务发布
        /// </summary>
        /// <param name="service">ICommunityTaskService</param>
        public CommunityTaskController(ICommunityTaskService service)
        {
            _service = service;
        }

        /// <summary>
        /// GetList
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns>
        [HttpGet("getlist")]
        public IActionResult GetList([FromQuery]CommunityTaskInputDto input)
        {
            var result = _service.GetListCommunityTask(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 获取数据
        /// </summary>
        /// <param name="id">id</param>
        /// <returns></returns>
        [HttpGet("{id}")]
        public IActionResult GetCommunityTask(string id)
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
        public IActionResult DeleteCommunityTask(string id)
        {
            var result = _service.Delete(id);
            return DeleteResponse(id, result);
        }

        /// <summary>
        /// 发布任务
        /// </summary>
        /// <param name="input">任务信息</param>
        /// <returns></returns>
        [HttpPost("add")]
        public IActionResult AddCommunityTask([FromBody]CommunityTaskInputDto input)
        {
            var result = _service.AddCommunityTask(input);
            return AddResponse(result);
        }
    }
}