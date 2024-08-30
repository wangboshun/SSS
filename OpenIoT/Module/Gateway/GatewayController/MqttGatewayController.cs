using Common.Utils;
using Furion.DynamicApiController;
using GatewayApplication.MQTT;
using GatewayEntity.MQTT.Dto;
using Microsoft.AspNetCore.Mvc;


namespace GatewayController
{
    [ApiDescriptionSettings("网关服务", Tag = "MQTT网关服务")]
    [Route("gateway/mqtt")]
    public class MqttGatewayController : IDynamicApiController
    {
        private readonly MqttGatewayService _mqttGatewayService;

        public MqttGatewayController(MqttGatewayService mqttGatewayService)
        {
            _mqttGatewayService = mqttGatewayService;
        }

        /// <summary>
        /// 开启服务
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        [HttpPost("start")]
        public string Start([FromBody] MtttGatewayStartDto input)
        {
            _mqttGatewayService.Start(input.Id, input.Host, input.Port);
            return "ok";
        }

        /// <summary>
        /// 关闭服务
        /// </summary>
        /// <returns></returns>
        [HttpPost("stop/{id}")]
        public string Stop([FromRoute] string id)
        {
            _mqttGatewayService.Stop(id);
            return "ok";
        }

        /// <summary>
        /// 获取已连接客户端
        /// </summary>
        /// <returns></returns>
        [HttpGet("{id}/clients")]
        public object GetClients([FromRoute] string id)
        {
            var result = _mqttGatewayService.GetClients(id);
            return result != null ? ResponseUtils.Ok(result) : ResponseUtils.Fail();
        }

        /// <summary>
        /// 踢下线
        /// </summary>
        /// <returns></returns>
        [HttpPost("ko")]
        public string KO([FromBody] MqttGatewayKOInputDto input)
        {
            _mqttGatewayService.KO(input);
            return "ok";
        }
    }
}