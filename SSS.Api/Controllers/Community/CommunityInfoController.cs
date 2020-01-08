using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Community.CommunityInfo.Service;
using SSS.Domain.Community.CommunityBusinessRelation.Dto;
using SSS.Domain.Community.CommunityInfo.Dto;

namespace SSS.Api.Controllers.Community
{
    /// <summary>
    /// 社区信息
    /// </summary> 
    [ApiVersion("4.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class CommunityInfoController : ApiBaseController
    {
        private readonly ICommunityInfoService _service;

        /// <summary>
        /// CommunityInfoController
        /// </summary>
        /// <param name="service">ICommunityInfoService</param>
        public CommunityInfoController(ICommunityInfoService service)
        {
            _service = service;
        }

        /// <summary>
        /// 所有社区信息
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns> 
        [HttpGet("getlist")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetList([FromQuery]CommunityInfoInputDto input)
        {
            var result = _service.GetListCommunityInfo(input);
            return PageResponse(result);
        }

        /// <summary>
        /// 获取数据
        /// </summary>
        /// <param name="id">id</param>
        /// <returns></returns> 
        [HttpGet("{id}")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetCommunityInfo(string id)
        {
            var result = _service.Get(id);
            return ApiResponse(result);
        }

        /// <summary>
        /// 添加承接业务
        /// </summary>
        /// <param name="input">业务Id和社区Id</param>
        /// <returns></returns> 
        [HttpPost("addbusiness")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddCommunityBusiness([FromBody]CommunityBusinessRelationInputDto input)
        {
            var result = _service.AddCommunityBusinessRelation(input);
            return AddResponse(result);
        }

        /// <summary>
        /// 删除数据
        /// </summary>
        /// <param name="id">id</param>
        /// <returns></returns> 
        [HttpDelete("{id}")]
        [AllowAnonymous]  //匿名访问
        public IActionResult DeleteCommunityInfo(string id)
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
        public IActionResult DeleteCommunityInfo([FromBody]CommunityInfoInputDto input)
        {
            var result = _service.UpdateCommunityInfo(input);
            return UpdateResponse(result, result);
        }

        /// <summary>
        /// 添加社区
        /// </summary>
        /// <param name="input">CommunityInfoInputDto</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddCommunityInfo([FromBody]CommunityInfoInputDto input)
        {
            var result = _service.AddCommunityInfo(input);
            return AddResponse(result);
        }
    }
}
