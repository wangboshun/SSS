﻿using System.Text;
using Common.Utils;
using DeviceEntity;
using Furion.DependencyInjection;
using Furion.DistributedIDGenerator;
using Furion.EventBus;
using Furion.JsonSerialization;
using GatewayEntity;
using MQTTnet;
using MQTTnet.Server;

namespace GatewayApplication.MQTT
{
    public class MqttGateway : ITransient
    {
        private static Dictionary<string, MqttServer> MQTT_SERVER_DICT = new Dictionary<string, MqttServer>();
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
                mqttServer.ValidatingConnectionAsync += ValidatingConnectionAsync; //验证
                mqttServer.ClientConnectedAsync += ClientConnectedAsync; //客户端连接成功
                mqttServer.ClientDisconnectedAsync += ClientDisconnectedAsync; //客户端断开连接
                mqttServer.ClientSubscribedTopicAsync += ClientSubscribedTopicAsync; //客户端订阅topic
                mqttServer.ClientUnsubscribedTopicAsync += ClientUnsubscribedTopicAsync; //客户端取消订阅topic
                mqttServer.InterceptingPublishAsync += InterceptingPublishAsync; //
                mqttServer.ClientAcknowledgedPublishPacketAsync += ClientAcknowledgedPublishPacketAsync; //7
                mqttServer.InterceptingSubscriptionAsync += InterceptingSubscriptionAsync; //8
                mqttServer.InterceptingUnsubscriptionAsync += InterceptingUnsubscriptionAsync; //9
                mqttServer.ApplicationMessageEnqueuedOrDroppedAsync += ApplicationMessageEnqueuedOrDroppedAsync; //10
                mqttServer.QueuedApplicationMessageOverwrittenAsync += QueuedApplicationMessageOverwrittenAsync; //11
                mqttServer.RetainedMessagesClearedAsync += RetainedMessagesClearedAsync; //12
                mqttServer.RetainedMessageChangedAsync += RetainedMessageChangedAsync; //13
                mqttServer.LoadingRetainedMessageAsync += LoadingRetainedMessageAsync; //14
                mqttServer.InterceptingOutboundPacketAsync += InterceptingOutboundPacketAsync; //15
                mqttServer.InterceptingInboundPacketAsync += InterceptingInboundPacketAsync; //16
                mqttServer.InterceptingClientEnqueueAsync += InterceptingClientEnqueueAsync; //17
                mqttServer.StartedAsync += StartedAsync; //开始
                mqttServer.ApplicationMessageNotConsumedAsync += ApplicationMessageNotConsumedAsync;
                mqttServer.PreparingSessionAsync += PreparingSessionAsync;
                mqttServer.SessionDeletedAsync += SessionDeletedAsync;
                mqttServer.StoppedAsync += StoppedAsync; //停止

                await Task.Run(async () =>
                {
                    await mqttServer.StartAsync();
                    MQTT_SERVER_DICT.Add(id, mqttServer);
                });
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        /// <summary>
        /// 停止服务
        /// </summary>
        /// <param name="arg"></param>
        /// <returns></returns>
        private Task StoppedAsync(EventArgs arg)
        {
            Console.WriteLine($"停止服务  StoppedAsync--->{JSON.Serialize(arg)}");
            return Task.CompletedTask;
        }

        private Task SessionDeletedAsync(SessionDeletedEventArgs arg)
        {
            Console.WriteLine($"SessionDeletedAsync--->{JSON.Serialize(arg)}");
            return Task.CompletedTask;
        }

        private Task PreparingSessionAsync(EventArgs arg)
        {
            Console.WriteLine($"PreparingSessionAsync--->{JSON.Serialize(arg)}");
            return Task.CompletedTask;
        }

        /// <summary>
        /// 应用程序消息
        /// 表明一个MQTT应用消息已经被发布了，但是没有被任何订阅的客户端消费掉
        /// </summary>
        /// <param name="arg"></param>
        /// <returns></returns>
        private Task ApplicationMessageNotConsumedAsync(ApplicationMessageNotConsumedEventArgs arg)
        {
            Console.WriteLine($"应用程序消息未使用  ApplicationMessageNotConsumedAsync--->{JSON.Serialize(arg)}");
            return Task.CompletedTask;
        }

        /// <summary>
        /// 应用程序消息
        /// 表明一个MQTT应用消息已经被发布了，已经被订阅的客户端消费掉
        /// </summary>
        /// <param name="arg"></param>
        /// <returns></returns>
        private Task ApplicationMessageEnqueuedOrDroppedAsync(ApplicationMessageEnqueuedEventArgs arg)
        {
            Console.WriteLine($"ApplicationMessageEnqueuedOrDroppedAsync--->{JSON.Serialize(arg)}");
            return Task.CompletedTask;
        }

        /// <summary>
        /// 拦截客户端队列
        /// </summary>
        /// <param name="arg"></param>
        /// <returns></returns>
        private Task InterceptingClientEnqueueAsync(InterceptingClientApplicationMessageEnqueueEventArgs arg)
        {
            Console.WriteLine($"拦截客户端队列  InterceptingClientEnqueueAsync--->{JSON.Serialize(arg)}");
            return Task.CompletedTask;
        }

        /// <summary>
        /// 拦截进站数据包
        /// </summary>
        /// <param name="arg"></param>
        /// <returns></returns>
        private Task InterceptingInboundPacketAsync(InterceptingPacketEventArgs arg)
        {
            Console.WriteLine($"拦截进站数据包  InterceptingInboundPacketAsync--->{JSON.Serialize(arg)}");
            return Task.CompletedTask;
        }

        /// <summary>
        /// 拦截出站数据包
        /// </summary>
        /// <param name="arg"></param>
        /// <returns></returns>
        private Task InterceptingOutboundPacketAsync(InterceptingPacketEventArgs arg)
        {
            Console.WriteLine($"拦截出站数据包  InterceptingOutboundPacketAsync--->{JSON.Serialize(arg)}");
            return Task.CompletedTask;
        }

        /// <summary>
        /// 加载保留消息
        /// </summary>
        /// <param name="arg"></param>
        /// <returns></returns>
        private Task LoadingRetainedMessageAsync(LoadingRetainedMessagesEventArgs arg)
        {
            Console.WriteLine($"加载保留消息 LoadingRetainedMessageAsync--->{JSON.Serialize(arg)}");
            return Task.CompletedTask;
        }

        /// <summary>
        /// 保留消息切换
        /// </summary>
        /// <param name="arg"></param>
        /// <returns></returns>
        private Task RetainedMessageChangedAsync(RetainedMessageChangedEventArgs arg)
        {
            Console.WriteLine($"保留消息切换  RetainedMessageChangedAsync--->{JSON.Serialize(arg)}");
            return Task.CompletedTask;
        }

        /// <summary>
        /// 清除保留消息
        /// </summary>
        /// <param name="arg"></param>
        /// <returns></returns>
        private Task RetainedMessagesClearedAsync(EventArgs arg)
        {
            Console.WriteLine($"清除保留消息  RetainedMessagesClearedAsync--->{JSON.Serialize(arg)}");
            return Task.CompletedTask;
        }

        private Task QueuedApplicationMessageOverwrittenAsync(QueueMessageOverwrittenEventArgs arg)
        {
            Console.WriteLine($"QueuedApplicationMessageOverwrittenAsync--->{JSON.Serialize(arg)}");
            return Task.CompletedTask;
        }

        /// <summary>
        /// 取消拦截订阅
        /// </summary>
        /// <param name="arg"></param>
        /// <returns></returns>
        private Task InterceptingUnsubscriptionAsync(InterceptingUnsubscriptionEventArgs arg)
        {
            Console.WriteLine($"取消拦截订阅  InterceptingUnsubscriptionAsync--->{JSON.Serialize(arg)}");
            return Task.CompletedTask;
        }

        /// <summary>
        /// 拦截订阅
        /// </summary>
        /// <param name="arg"></param>
        /// <returns></returns>
        private Task InterceptingSubscriptionAsync(InterceptingSubscriptionEventArgs arg)
        {
            Console.WriteLine($"拦截订阅  InterceptingSubscriptionAsync--->{JSON.Serialize(arg)}");
            return Task.CompletedTask;
        }

        /// <summary>
        /// 已确认发布数据包
        /// </summary>
        /// <param name="arg"></param>
        /// <returns></returns>
        private Task ClientAcknowledgedPublishPacketAsync(ClientAcknowledgedPublishPacketEventArgs arg)
        {
            Console.WriteLine($"已确认发布数据包  ClientAcknowledgedPublishPacketAsync--->{JSON.Serialize(arg)}");
            return Task.CompletedTask;
        }

        /// <summary>
        /// 拦截客户端的消息
        /// </summary>
        /// <param name="arg"></param>
        /// <returns></returns>
        private async Task InterceptingPublishAsync(InterceptingPublishEventArgs arg)
        {
            Console.WriteLine($"拦截客户端的消息  InterceptingPublishAsync--->{JSON.Serialize(arg)}");
            var msg = Encoding.UTF8.GetString(arg.ApplicationMessage.PayloadSegment); 
            var router = arg.ApplicationMessage.Topic?.Split("/", StringSplitOptions.RemoveEmptyEntries);
            if (router is { Length: > 3 })
            {
                var type = router[2];
                var reportType = "";
                if (type.Equals("properties"))
                {
                    var action = router[3];
                    if (action.Equals("report"))
                    {
                        reportType = "REPORT";
                        ReportEntity reportEntity = new ReportEntity
                        {
                            DeviceId = arg.ClientId,
                            MsgType = "REPORT",
                            Content = msg,
                            TM = DateTime.Now
                        };
                        reportEntity.Id = TimeUtils.DateTimeToString(reportEntity.TM, false, false, true);
                        await _eventPublisher.PublishAsync("Mqtt:PropertiesReport", JSON.Serialize(reportEntity));
                    }
                }
                
                //设备影子
                DeviceShadowEntity shadowEntity = new DeviceShadowEntity
                {
                    DeviceId = arg.ClientId,
                    MsgType = reportType, 
                    Content = msg,
                    CreateTime = DateTime.Now
                };
                await _eventPublisher.PublishAsync("Device:Shadow", JSON.Serialize(shadowEntity));
            }
        }

        /// <summary>
        /// 客户端取消订阅
        /// </summary>
        /// <param name="arg"></param>
        /// <returns></returns>
        private Task ClientUnsubscribedTopicAsync(ClientUnsubscribedTopicEventArgs arg)
        {
            Console.WriteLine($"客户端取消订阅 ClientUnsubscribedTopicAsync--->{JSON.Serialize(arg)}");
            return Task.CompletedTask;
        }

        /// <summary>
        /// 客户端订阅Topic
        /// </summary>
        /// <param name="arg"></param>
        /// <returns></returns>
        private Task ClientSubscribedTopicAsync(ClientSubscribedTopicEventArgs arg)
        {
            Console.WriteLine($"客户端订阅Topic  ClientSubscribedTopicAsync--->{JSON.Serialize(arg)}");
            return Task.CompletedTask;
        }

        /// <summary>
        /// 开启服务
        /// </summary>
        /// <param name="arg"></param>
        /// <returns></returns>
        private Task StartedAsync(EventArgs arg)
        {
            Console.WriteLine($"开启服务  StartedAsync--->{JSON.Serialize(arg)}");
            return Task.CompletedTask;
        }

        /// <summary>
        /// 客户端断开连接
        /// </summary>
        /// <param name="arg"></param>
        /// <returns></returns>
        private async Task ClientDisconnectedAsync(ClientDisconnectedEventArgs arg)
        {
            Console.WriteLine($"客户端断开连接  ClientDisconnectedAsync--->{JSON.Serialize(arg)}");
            ReportEntity reportEntity = new ReportEntity
            {
                DeviceId = arg.ClientId,
                MsgType = "OFFLINE",
                IP = arg.Endpoint,
                TM = DateTime.Now
            };
            reportEntity.Id = TimeUtils.DateTimeToString(reportEntity.TM, false, false, true);
            await _eventPublisher.PublishAsync("Mqtt:Offline", JSON.Serialize(reportEntity));
        }

        /// <summary>
        /// 客户端连接成功
        /// </summary>
        /// <param name="arg"></param>
        /// <returns></returns>
        private async Task ClientConnectedAsync(ClientConnectedEventArgs arg)
        {
            Console.WriteLine($"客户端连接成功  ClientConnectedAsync--->{JSON.Serialize(arg)}");
            ReportEntity reportEntity = new ReportEntity
            {
                DeviceId = arg.ClientId,
                MsgType = "ONLINE",
                IP =arg.Endpoint,
                TM = DateTime.Now
            };
            reportEntity.Id = TimeUtils.DateTimeToString(reportEntity.TM, false, false, true);
            await _eventPublisher.PublishAsync("Mqtt:Online", JSON.Serialize(reportEntity));
        }

        /// <summary>
        /// 连接验证
        /// </summary>
        /// <param name="arg"></param>
        /// <returns></returns>
        private Task ValidatingConnectionAsync(ValidatingConnectionEventArgs arg)
        {
            Console.WriteLine($"连接验证  ValidatingConnectionAsync--->{JSON.Serialize(arg)}");
            var userName = arg.UserName;
            var passWord = arg.Password;
            var deviceId = arg.ClientId;
            return Task.CompletedTask;
        }

        public void Stop(string id)
        {
        }
    }
}