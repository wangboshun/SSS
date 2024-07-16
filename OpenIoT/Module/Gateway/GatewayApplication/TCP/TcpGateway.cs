using System.Net;
using System.Net.Sockets;
using System.Text;
using Furion.DependencyInjection;
using GatewayEntity.TCP;

namespace GatewayApplication.TCP
{
    public class TcpGateway : ITransient
    {
        private static Dictionary<string, TcpListener> TCP_SERVER_DICT = new();
        private static Dictionary<string, string> CLIENT_DICT = new();

        public async void Start(string id, string host, int port)
        {
            IPAddress address = IPAddress.Parse(host);
            TcpListener listener = new TcpListener(address, port);
            listener.Start();
            TCP_SERVER_DICT.Add(id, listener);

            while (true)
            {
                if (listener.Pending())
                {
                    TcpClient client = await listener.AcceptTcpClientAsync();
                    var ip = client.Client.RemoteEndPoint;
                    await Task.Run(() =>
                    {
                        NetworkStream stream = client.GetStream();
                        while (true)
                        {
                            if (stream.DataAvailable)
                            {
                                byte[] data = new byte[1024];
                                int len = stream.Read(data, 0, 1024);
                                string msg = Encoding.UTF8.GetString(data, 0, len);
                            }

                            if (!IsOnline(client))
                            {
                                Console.WriteLine("Connect Closed.");
                                break;
                            }

                            Thread.Sleep(1);
                        }
                    });
                }

                Thread.Sleep(1);
            }
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

        /// <summary>
        /// 是否在线
        /// </summary>
        /// <param name="client"></param>
        /// <returns></returns>
        private bool IsOnline(TcpClient client)
        {
            return !((client.Client.Poll(15000, SelectMode.SelectRead) && (client.Client.Available == 0)) ||
                     !client.Client.Connected);
        }
    }
}