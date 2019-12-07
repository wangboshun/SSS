using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.Info.OperateInfo.Service;
using SSS.Domain.Permission.Info.OperateInfo.Dto;
using SSS.Domain.Permission.Relation.PowerGroupOperateRelation.Dto;

namespace SSS.Api.Controllers.Permission.Info
{
    /// <summary>
    /// 操作信息
    /// </summary> 
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class OperateInfoController : ApiBaseController
    {
        private readonly IOperateInfoService _service;

        /// <summary>
        /// 操作信息
        /// </summary>
        /// <param name="service">IOperateInfoService</param>
        public OperateInfoController(IOperateInfoService service)
        {
            _service = service;
        }

        /// <summary>
        ///     获取所有操作信息
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns>
        [HttpGet("getlist")]
        [AllowAnonymous] //匿名访问
        public IActionResult GetList([FromQuery] OperateInfoInputDto input)
        {
            var result = _service.GetListOperateInfo(input);
            return ApiResponse(result);
        }

        /// <summary>
        /// 添加操作
        /// </summary>
        /// <param name="input">操作名称</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddOperateInfo([FromBody]OperateInfoInputDto input)
        {
            _service.AddOperateInfo(input);
            return ApiResponse(input);
        }

        /// <summary>
        /// 删除操作信息
        /// </summary>
        /// <param name="input">OperateInfoInputDto</param>
        /// <returns></returns> 
        [HttpPost("delete")]
        [AllowAnonymous]  //匿名访问
        public IActionResult DeleteUserInfo([FromBody]OperateInfoInputDto input)
        {
            _service.DeleteOperateInfo(input);
            return ApiResponse(input);
        }

        /// <summary>
        ///     获取操作下的所有下级
        /// </summary>
        /// <param name="operateid">操作Id</param>
        /// <returns></returns>
        [HttpGet("get_children_by_operateid")]
        [AllowAnonymous] //匿名访问
        public IActionResult GetChildren(string operateid)
        {
            var result = _service.GetChildrenById(operateid);
            return ApiResponse(result);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联操作
        /// </summary>
        /// <param name="input">权限组Id或名称</param>
        /// <returns></returns> 
        [HttpGet("get_operate_by_powergroup")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetOperateByPowerGroup([FromQuery]PowerGroupOperateRelationInputDto input)
        {
            var result = _service.GetOperateByPowerGroup(input);
            return ApiResponse(result);
        }
    }
}
