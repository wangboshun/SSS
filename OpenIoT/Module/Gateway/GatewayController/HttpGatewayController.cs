using Furion.DynamicApiController;

using GatewayApplication.HTTP;

using GatewayEntity.HTTP.Dto;

using Microsoft.AspNetCore.Mvc;


namespace GatewayController
{
    [ApiDescriptionSettings("网关服务", Tag = "HTTP网关服务")]
    [Route("gateway/http")]
    public class HttpGatewayController : IDynamicApiController
    {
        private readonly HttpGatewayService _httpGatewayService;

        public HttpGatewayController(HttpGatewayService httpGatewayService)
        {
            _httpGatewayService = httpGatewayService;
        }

        /// <summary>
        /// 开启服务
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        [HttpPost("start")]
        public string Start([FromBody] HttpGatewayStartDto input)
        {
            _httpGatewayService.Start(input.Id, input.Host, input.Port); 
            return "ok";
        }

        /// <summary>
        /// 关闭服务
        /// </summary>
        /// <returns></returns>
        [HttpPost("stop/{id}")]
        public string Stop([FromRoute] string id)
        {
            _httpGatewayService.Stop(id);
            return "ok";
        }
    }
}
