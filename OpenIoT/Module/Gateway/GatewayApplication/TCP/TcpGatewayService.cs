using System.Collections.Concurrent;
using System.Text;
using Common.Utils;
using Furion.DependencyInjection;
using Furion.EventBus;
using Furion.JsonSerialization;
using GatewayEntity;
using GatewayEntity.TCP;
using Newtonsoft.Json.Linq;
using TouchSocket.Core;
using TouchSocket.Sockets;

namespace GatewayApplication.TCP
{
    /// <summary>
    /// TCP网关服务
    /// </summary>
    public class TcpGatewayService : ITransient
    {
        private static ConcurrentDictionary<string, DefaultTcpService> TCP_SERVER_DICT = new();
        private static ConcurrentDictionary<string, DateTime> CLIENT_TIME_DICT = new();
        private static ConcurrentDictionary<string, int> MSG_COUNT_DICT = new();
        private static ConcurrentDictionary<string, int> DATA_SIZE_DICT = new();
        private static ConcurrentDictionary<string, bool> LOGIN_DICT = new();

        private readonly IEventPublisher _eventPublisher;

        public TcpGatewayService(IEventPublisher eventPublisher)
        {
            _eventPublisher = eventPublisher;
        }

        public async void Start(string id, string host, int port)
        {
            await Task.Run(async () =>
            {
                try
                {
                    var service = new DefaultTcpService();
                    service.Connecting = Connecting;
                    service.Connected = Connected;
                    service.Disconnecting = Disconnecting;
                    service.Disconnected = Disconnected;
                    service.Received = Received;
                    await service.SetupAsync(new TouchSocketConfig()
                        .SetServerName(id)
                        .SetListenOptions(option =>
                        {
                            option.Add(new TcpListenOption()
                            {
                                IpHost = $"{host}:{port}"
                            });
                        }));

                    await service.StartAsync();
                    TCP_SERVER_DICT.TryAdd(id, service);
                }
                catch (Exception e)
                {
                    Console.WriteLine(e);
                }
            });
        }

        /// <summary>
        /// 从客户端收到信息
        /// </summary>
        /// <param name="client"></param>
        /// <param name="e"></param>
        /// <returns></returns> 
        private async Task Received(SocketClient client, ReceivedDataEventArgs e)
        {
            CLIENT_TIME_DICT.AddOrUpdate(client.Id + "_Last", DateTime.Now,
                (k, v) => DateTime.Now);
            MSG_COUNT_DICT.AddOrUpdate(client.Id + "_Receive", 1,
                (k, v) => v + 1);
            DATA_SIZE_DICT.AddOrUpdate(client.Id + "_Receive", e.ByteBlock.Len,
                (k, v) => v + e.ByteBlock.Len);
            var text = Encoding.UTF8.GetString(e.ByteBlock.Buffer, 0, e.ByteBlock.Len);
            if (!string.IsNullOrWhiteSpace(text))
            {
                JObject o = JObject.Parse(text);
                string type = o["type"].ToString();
                if (type.Equals("report"))
                {
                    // {"type":"report","data":"abcd"}
                    if (LOGIN_DICT.TryGetValue(client.Id, out var v1))
                    {
                        if (v1)
                        {
                            string data = o["data"].ToString();
                            ReportEntity reportEntity = new ReportEntity
                            {
                                DeviceId = client.Id,
                                MsgType = "REPORT",
                                Content = data,
                                TM = DateTime.Now
                            };
                            reportEntity.Id = TimeUtils.DateTimeToString(reportEntity.TM, false, false, true);
                            await _eventPublisher.PublishAsync("Tcp:PropertiesReport", JSON.Serialize(reportEntity));
                        }
                        else
                        {
                            client.Close();
                        }
                    }
                    else
                    {
                        client.Close();
                    }
                }
                else if (type.Equals("login"))
                {
                    // {"type":"login","user":"admin","password":"123456","device_id":"test_1"}
                    string user = "";
                    string password = "";
                    string deviceId = "";
                    if (o.TryGetValue("user", value: out var v1))
                    {
                        user = v1.ToString();
                    }

                    if (o.TryGetValue("password", out var v2))
                    {
                        password = v2.ToString();
                    }

                    if (o.TryGetValue("device_id", out var v3))
                    {
                        deviceId = v3.ToString();
                    }

                    if (user.Equals("admin") && password.Equals("123456"))
                    {
                        client.ResetId(deviceId);
                        ReportEntity reportEntity = new ReportEntity
                        {
                            DeviceId = deviceId,
                            MsgType = "ONLINE",
                            IP = client.GetIPPort(),
                            TM = DateTime.Now
                        };
                        LOGIN_DICT.TryAdd(client.Id, true);
                        reportEntity.Id = TimeUtils.DateTimeToString(reportEntity.TM, false, false, true);
                        await _eventPublisher.PublishAsync("Tcp:Online", JSON.Serialize(reportEntity));
                    }
                    else
                    {
                        client.Close();
                    }
                }
                else
                {
                    if (LOGIN_DICT.TryGetValue(client.Id, out var v1))
                    {
                        if (!v1)
                        {
                            client.Close();
                        }
                    }
                    else
                    {
                        client.Close();
                    }
                }
            }
        }

        /// <summary>
        /// 有客户端断开连接
        /// </summary>
        /// <param name="client"></param>
        /// <param name="e"></param>
        /// <returns></returns> 
        private async Task Disconnected(SocketClient client, DisconnectEventArgs e)
        {
            CLIENT_TIME_DICT.TryRemove(client.Id + "_Connect", out var v1);
            CLIENT_TIME_DICT.TryRemove(client.Id + "_Last", out var v2);
            MSG_COUNT_DICT.TryRemove(client.Id + "_Receive", out var v3);
            DATA_SIZE_DICT.TryRemove(client.Id + "_Receive", out var v4);

            ReportEntity reportEntity = new ReportEntity
            {
                DeviceId = client.Id,
                MsgType = "OFFLINE",
                IP = client.GetIPPort(),
                TM = DateTime.Now
            };
            reportEntity.Id = TimeUtils.DateTimeToString(reportEntity.TM, false, false, true);
            await _eventPublisher.PublishAsync("Tcp:Offline", JSON.Serialize(reportEntity));
        }

        /// <summary>
        /// 有客户端正在断开连接，只有当主动断开时才有效
        /// </summary>
        /// <param name="client"></param>
        /// <param name="e"></param>
        /// <returns></returns> 
        private Task Disconnecting(SocketClient client, DisconnectEventArgs e)
        {
            return Task.CompletedTask;
        }

        /// <summary>
        /// 有客户端成功连接
        /// </summary>
        /// <param name="client"></param>
        /// <param name="e"></param>
        /// <returns></returns> 
        private Task Connected(SocketClient client, ConnectedEventArgs e)
        {
            CLIENT_TIME_DICT.TryAdd(client.Id + "_Connect", DateTime.Now);
            CLIENT_TIME_DICT.TryAdd(client.Id + "_Last", DateTime.Now);
            return Task.CompletedTask;
        }

        /// <summary>
        /// 有客户端正在连接
        /// </summary>
        /// <param name="client"></param>
        /// <param name="e"></param>
        /// <returns></returns>
        private Task Connecting(SocketClient client, ConnectingEventArgs e)
        {
            return Task.CompletedTask;
        }

        public void Stop(string id)
        {
            if (TCP_SERVER_DICT.TryRemove(id, out DefaultTcpService? service))
            {
                service.Stop();
            }
        }

        public List<TcpClientOutDto>? GetClients(string id)
        {
            List<TcpClientOutDto> list = [];
            if (!TCP_SERVER_DICT.TryGetValue(id, out var tcpService)) return null;
            var clientList = tcpService.GetIds();
            foreach (var item in clientList)
            {
                if (!tcpService.TryGetSocketClient(item, out var client)) continue;

                DATA_SIZE_DICT.TryGetValue(client.Id + "_Receive", out var receiveSize);
                MSG_COUNT_DICT.TryGetValue(client.Id + "_Receive", out var receiveCount);
                CLIENT_TIME_DICT.TryGetValue(client.Id + "_Last", out var lastTime);
                CLIENT_TIME_DICT.TryGetValue(client.Id + "_Connect", out var connectTime);

                var model = new TcpClientOutDto()
                {
                    ClientId = client.Id,
                    IP = client.GetIPPort(),
                    ReceiveSize = receiveSize,
                    ReceiveMessageCount = receiveCount,
                    ConnectTime = connectTime,
                    LastTime = lastTime,
                };
                list.Add(model);
            }

            return list;
        }

        public void KO(TcpGatewayKOInputDto dto)
        {
            TCP_SERVER_DICT.TryGetValue(dto.ServerId, out var server);
            if (server == null) return;
            var clientList = server.GetIds();
            if (clientList == null || !clientList.Any(x => x.Equals(dto.ClientId))) return;
            if (server.TryGetSocketClient(dto.ClientId, out var client))
            {
                client.Close();
            }
        }
    }
}