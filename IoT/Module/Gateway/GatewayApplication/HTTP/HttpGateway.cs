using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

using Furion.DependencyInjection;
using Furion.DistributedIDGenerator;

using Microsoft.AspNetCore.Http.HttpResults;

namespace GatewayApplication.HTTP
{
    public class HttpGateway : ITransient
    {
        private static Dictionary<string, HttpListener> dict = new Dictionary<string, HttpListener>();

        public HttpGateway()
        {

        }

        public async void OpenService(string host, int port)
        {
            try
            {
                var id = ShortIDGen.NextID();
                HttpListener httpListener = new HttpListener();
                httpListener.Prefixes.Add($"http://{host}:{port}/");
                httpListener.Start();
                dict.Add(id, httpListener);
                await Task.Run(() => Listen(httpListener));
            }
            catch (Exception e)
            {

            }
        }

        public void CloseService(string id)
        {
            if (dict.TryGetValue(id, out HttpListener? httpListener))
            {
                httpListener.Close();
            }
        }

        private async Task Listen(HttpListener httpListener)
        {
            while (httpListener.IsListening)
            {
                var context = await httpListener.GetContextAsync();
                var request = context.Request;
                using (Stream body = request.InputStream)
                {
                    using (StreamReader reader = new StreamReader(body, Encoding.UTF8))
                    {
                        string content = await reader.ReadToEndAsync();
                        StringBuilder sb = new StringBuilder();
                        sb.AppendLine($"Url:{request.Url}");
                        sb.AppendLine($"RemoteIP:{request.RemoteEndPoint}");
                        sb.AppendLine($"Body:{content}");
                        Console.WriteLine($"Request---> {sb.ToString()}");
                    }
                }

                var response = context.Response;
                using (Stream body = response.OutputStream)
                {
                    using (StreamWriter writer = new StreamWriter(body, Encoding.UTF8))
                    {
                        writer.WriteLine("OK");
                        StringBuilder sb = new StringBuilder();
                        sb.AppendLine($"RemoteIP:{request.RemoteEndPoint}");
                        sb.AppendLine($"Result:OK");
                        Console.WriteLine($"Response---> {sb.ToString()}");
                    }
                }
            }
        }
    }
}
