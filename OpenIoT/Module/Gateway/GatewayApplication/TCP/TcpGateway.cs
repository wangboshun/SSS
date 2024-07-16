using System.Net.Sockets;
using System.Text;
using Furion.DependencyInjection;
using GatewayEntity.TCP;
using TouchSocket.Core;
using TouchSocket.Sockets;

namespace GatewayApplication.TCP
{
    public class TcpGateway : ITransient
    {
        private static Dictionary<string, TcpListener> TCP_SERVER_DICT = new();
        private static Dictionary<string, string> CLIENT_DICT = new();

        public async void Start(string id, string host, int port)
        {
            var service = new TcpService();
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
                        IpHost = $"{host}:{port}",
                        Name = id,
                    });
                }));

            await service.StartAsync();
        }

        /// <summary>
        /// 从客户端收到信息
        /// </summary>
        /// <param name="client"></param>
        /// <param name="e"></param>
        /// <returns></returns> 
        private Task Received(SocketClient client, ReceivedDataEventArgs e)
        {
            var msg = Encoding.UTF8.GetString(e.ByteBlock.Buffer, 0, e.ByteBlock.Len);
            return Task.CompletedTask;
        }

        /// <summary>
        /// 有客户端断开连接
        /// </summary>
        /// <param name="client"></param>
        /// <param name="e"></param>
        /// <returns></returns> 
        private Task Disconnected(SocketClient client, DisconnectEventArgs e)
        {
            return Task.CompletedTask;
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
        }

        public List<TcpClientOutDto>? GetClients(string id)
        {
            return new List<TcpClientOutDto>();
        }

        public void KO(string id)
        {
        }
    }
}