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
        private readonly HttpGateway _httpGateway;

        public HttpGatewayController(HttpGateway httpGateway)
        {
            _httpGateway = httpGateway;
        }

        /// <summary>
        /// 开启服务
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        [HttpPost("open")]
        public string OpenService([FromBody] HttpGatewayOpenDto input)
        {
            _httpGateway.OpenService(input.Host, input.Port);
            return "ok";
        }

        /// <summary>
        /// 关闭服务
        /// </summary>
        /// <returns></returns>
        [HttpPost("close/{id}")]
        public string CloseService([FromRoute] string id)
        {
            _httpGateway.CloseService(id);
            return "ok";
        }
    }
}
