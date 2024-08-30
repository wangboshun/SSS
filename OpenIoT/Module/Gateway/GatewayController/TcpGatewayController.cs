using Common.Utils;
using Furion.DynamicApiController;
using GatewayApplication.TCP; 
using GatewayEntity.TCP;
using Microsoft.AspNetCore.Mvc;


namespace GatewayController
{
    [ApiDescriptionSettings("网关服务", Tag = "TCP网关服务")]
    [Route("gateway/tcp")]
    public class TcpGatewayController : IDynamicApiController
    {
        private readonly TcpGatewayService _tcpGatewayService;

        public TcpGatewayController(TcpGatewayService tcpGatewayService)
        {
            _tcpGatewayService = tcpGatewayService;
        }

        /// <summary>
        /// 开启服务
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        [HttpPost("start")]
        public string Start([FromBody] TcpGatewayStartDto input)
        {
            _tcpGatewayService.Start(input.Id, input.Host, input.Port);
            return "ok";
        }

        /// <summary>
        /// 关闭服务
        /// </summary>
        /// <returns></returns>
        [HttpPost("stop/{id}")]
        public string Stop([FromRoute] string id)
        {
            _tcpGatewayService.Stop(id);
            return "ok";
        }

        /// <summary>
        /// 获取已连接客户端
        /// </summary>
        /// <returns></returns>
        [HttpGet("{id}/clients")]
        public object GetClients([FromRoute] string id)
        {
            var result = _tcpGatewayService.GetClients(id);
            return result != null ? ResponseUtils.Ok(result) : ResponseUtils.Fail();
        }

        /// <summary>
        /// 踢下线
        /// </summary>
        /// <returns></returns>
        [HttpPost("ko")]
        public string KO([FromBody] TcpGatewayKOInputDto input)
        {
            _tcpGatewayService.KO(input);
            return "ok";
        }
    }
}