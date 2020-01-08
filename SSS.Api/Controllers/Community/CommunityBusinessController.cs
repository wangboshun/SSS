using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Community.CommunityBusiness.Service;
using SSS.Domain.Community.CommunityBusiness.Dto;
using SSS.Domain.Community.CommunityInfo.Dto;

namespace SSS.Api.Controllers.Community
{
    /// <summary>
    /// 业务类型
    /// </summary> 
    [ApiVersion("4.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class CommunityBusinessController : ApiBaseController
    {
        private readonly ICommunityBusinessService _service;

        /// <summary>
        /// CommunityBusinessController
        /// </summary>
        /// <param name="service">ICommunityBusinessService</param>
        public CommunityBusinessController(ICommunityBusinessService service)
        {
            _service = service;
        }

        /// <summary>
        /// 所有业务
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns> 
        [HttpGet("getlist")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetList([FromQuery]CommunityBusinessInputDto input)
        {
            var result = _service.GetListCommunityBusiness(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 根据社区获取承接业务
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns> 
        [HttpGet("get_communitybusiness_by_community")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetCommunityBusinessByCommunity([FromQuery]CommunityInfoInputDto input)
        {
            var result = _service.GetCommunityBusinessByCommunity(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 获取数据
        /// </summary>
        /// <param name="id">id</param>
        /// <returns></returns> 
        [HttpGet("{id}")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetCommunityBusiness(string id)
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
        public IActionResult DeleteCommunityBusiness(string id)
        {
            var result = _service.Delete(id);
            return DeleteResponse(id, result);
        }

        /// <summary>
        /// 修改数据
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns> 
        [HttpPost("update")]
        [AllowAnonymous]  //匿名访问
        public IActionResult DeleteCommunityInfo([FromBody]CommunityBusinessInputDto input)
        {
            var result = _service.UpdateCommunityBusiness(input);
            return UpdateResponse(input, result);
        }

        /// <summary>
        /// 添加业务类型
        /// </summary>
        /// <param name="input">CommunityBusinessInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddCommunityBusiness([FromBody]CommunityBusinessInputDto input)
        {
            var result = _service.AddCommunityBusiness(input);
            return AddResponse(result);
        }
    }
}
