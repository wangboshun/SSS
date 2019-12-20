using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.Info.MenuInfo.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Info.MenuInfo.Dto;

namespace SSS.Api.Controllers.Permission.Info
{
    /// <summary>
    /// 菜单信息
    /// </summary> 
    [ApiVersion("2.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class MenuInfoController : ApiBaseController
    {
        private readonly IMenuInfoService _service;

        /// <summary>
        /// 菜单信息
        /// </summary>
        /// <param name="service">IMenuInfoService</param>
        public MenuInfoController(IMenuInfoService service)
        {
            _service = service;
        }

        /// <summary>
        /// 获取所有菜单信息
        /// </summary>
        /// <param name="input">input</param>
        /// <returns></returns> 
        [HttpGet("getlist")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetList([FromQuery]MenuInfoInputDto input)
        {
            var result = _service.GetListMenuInfo(input);
            return PageResponse(result);
        }

        /// <summary>
        ///     获取菜单下的所有下级
        /// </summary>
        /// <param name="menuid">菜单Id</param>
        /// <returns></returns>
        [HttpGet("get_children_by_menuid")]
        [AllowAnonymous] //匿名访问
        public IActionResult GetChildren(string menuid)
        {
            var result = _service.GetChildren(menuid);
            return ApiResponse(result);
        }

        /// <summary>
        /// 添加菜单
        /// </summary>
        /// <param name="input">菜单名称</param>
        /// <returns></returns> 
        [HttpPost("add")]
        [AllowAnonymous]  //匿名访问
        public IActionResult AddMenuInfo([FromBody]MenuInfoInputDto input)
        {
            var result = _service.AddMenuInfo(input);
            return AddResponse(result);
        }

        /// <summary>
        /// 删除菜单信息
        /// </summary>
        /// <param name="id">id</param>
        /// <returns></returns> 
        [HttpDelete("{id}")]
        [AllowAnonymous]  //匿名访问
        public IActionResult DeleteUserInfo(string id)
        {
            var result = _service.DeleteMenuInfo(id);
            return DeleteResponse(id, result);
        }

        /// <summary>
        /// 修改菜单
        /// </summary>
        /// <param name="input">菜单</param>
        /// <returns></returns> 
        [HttpPost("update")]
        [AllowAnonymous]  //匿名访问
        public IActionResult UpdateMenuInfo([FromBody]MenuInfoInputDto input)
        {
            var result = _service.UpdateMenuInfo(input);
            return UpdateResponse(input.id, result);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联菜单
        /// </summary>
        /// <param name="input">权限组Id或名称</param>
        /// <returns></returns> 
        [HttpGet("get_menu_by_powergroup")]
        [AllowAnonymous]  //匿名访问
        public IActionResult GetMenuByPowerGroup([FromQuery]PowerGroupInputDto input)
        {
            var result = _service.GetMenuByPowerGroup(input);
            return PageResponse(result);
        }
    }
}
