

using System.Net;

using Furion.DependencyInjection;
using Furion.DistributedIDGenerator;
using Furion.EventBus;

using Microsoft.Extensions.Options;

using MQTTnet;
using MQTTnet.Protocol;
using MQTTnet.Server;

namespace GatewayApplication.MQTT
{
    public class MqttGateway : ITransient
    {

        private static Dictionary<string, MqttServer> dict = new Dictionary<string, MqttServer>();
        private readonly IEventPublisher _eventPublisher;

        public MqttGateway(IEventPublisher eventPublisher)
        {
            _eventPublisher = eventPublisher;
        }
        public async void Start(string host, int port)
        {
            try
            {
                var id = ShortIDGen.NextID();
                var mqttFactory = new MqttFactory();
                var mqttServerOptions = new MqttServerOptionsBuilder()
                    .WithDefaultEndpoint() 
                    .WithDefaultEndpointPort(port).Build();
                 
                var mqttServer = mqttFactory.CreateMqttServer(mqttServerOptions);
                mqttServer.ValidatingConnectionAsync += ValidatingConnectionAsync;
                mqttServer.ClientConnectedAsync += ClientConnectedAsync;
                mqttServer.ClientDisconnectedAsync += ClientDisconnectedAsync;
                mqttServer.StartedAsync += StartedAsync;

                await Task.Run(async () =>
                {
                    await mqttServer.StartAsync();
                    dict.Add(id, mqttServer);
                });
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        private async Task StartedAsync(EventArgs args)
        {

        }

        /// <summary>
        /// 客户端断开连接
        /// </summary>
        /// <param name="args"></param>
        /// <returns></returns>
        private async Task ClientDisconnectedAsync(ClientDisconnectedEventArgs args)
        {

        }

        /// <summary>
        /// 客户端连接成功
        /// </summary>
        /// <param name="args"></param>
        /// <returns></returns>
        private async Task ClientConnectedAsync(ClientConnectedEventArgs args)
        {

        }

        /// <summary>
        /// 对客户端的连接进行验证
        /// </summary>
        /// <param name="args"></param>
        /// <returns></returns>
        private async Task ValidatingConnectionAsync(ValidatingConnectionEventArgs args)
        {
            var userName = args.UserName;
            var passWord = args.Password;
            var deviceId = args.ClientId;
        }

        public void Stop(string id)
        {

        }
    }
}
