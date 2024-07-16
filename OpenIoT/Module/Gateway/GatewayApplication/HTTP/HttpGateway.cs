using System.Net;
using System.Text;
using Common.Utils;
using DeviceEntity;
using Furion.DataEncryption;
using Furion.DependencyInjection;
using Furion.DistributedIDGenerator;
using Furion.EventBus;
using Furion.JsonSerialization;
using GatewayEntity;

namespace GatewayApplication.HTTP
{
    /// <summary>
    /// Http网关
    /// </summary>
    public class HttpGateway : ITransient
    {
        private static Dictionary<string, HttpListener> HTTP_DICT = new Dictionary<string, HttpListener>();
        private readonly IEventPublisher _eventPublisher;

        public HttpGateway(IEventPublisher eventPublisher)
        {
            _eventPublisher = eventPublisher;
        }

        public async void Start(string host, int port)
        {
            try
            {
                var id = ShortIDGen.NextID();
                HttpListener httpListener = new HttpListener();
                httpListener.Prefixes.Add($"http://{host}:{port}/");
                httpListener.Start();
                HTTP_DICT.Add(id, httpListener);
                await Task.Run(() => Listen(httpListener));
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        public void Stop(string id)
        {
            if (HTTP_DICT.TryGetValue(id, out HttpListener? httpListener))
            {
                httpListener.Close();
                HTTP_DICT.Remove(id);
            }
        }

        private async Task Listen(HttpListener httpListener)
        {
            while (httpListener.IsListening)
            {
                var context = await httpListener.GetContextAsync();
                var result = await RequestAsync(context);
                await ResponseAsync(context, result);
            }
        }

        private async Task<object> RequestAsync(HttpListenerContext context)
        {
            var request = context.Request;
            if (request.HttpMethod.Equals("POST"))
            {
                await using Stream body = request.InputStream;
                using StreamReader reader = new StreamReader(body, Encoding.UTF8);
                string content = await reader.ReadToEndAsync();
                var router = request.RawUrl?.Split("/", StringSplitOptions.RemoveEmptyEntries);

                //路由
                // {productId}/{deviceId}/online 上线
                // {productId}/{deviceId}}/properties/report 属性上报
                if (router is { Length: > 2 })
                {
                    // token检测
                    var (valid, code, msg) = Auth(request);
                    if (valid)
                    {
                        var produceId = router[0];
                        var deviceId = router[1];
                        var type = router[2];
                        var reportType = "";

                        // 上线
                        if (type.Equals("online"))
                        {
                            reportType = "ONLINE";
                            ReportEntity reportEntity = new ReportEntity
                            {
                                DeviceId = deviceId,
                                MsgType = reportType,
                                IP = request.RemoteEndPoint.ToString(),
                                TM = DateTime.Now
                            };
                            reportEntity.Id = TimeUtils.DateTimeToString(reportEntity.TM, false, false, true);
                            await _eventPublisher.PublishAsync("Http:Online", JSON.Serialize(reportEntity));
                        }
                        // 属性上报
                        else if (type.Equals("properties"))
                        {
                            var action = router[3];
                            if (action.Equals("report"))
                            {
                                reportType = "REPORT";
                                ReportEntity reportEntity = new ReportEntity
                                {
                                    DeviceId = deviceId,
                                    MsgType = reportType,
                                    Content = content,
                                    IP = request.RemoteEndPoint.ToString(),
                                    TM = DateTime.Now
                                };
                                reportEntity.Id = TimeUtils.DateTimeToString(reportEntity.TM, false, false, true);
                                await _eventPublisher.PublishAsync("Http:PropertiesReport", JSON.Serialize(reportEntity));
                            }
                        }

                        //设备影子
                        DeviceShadowEntity shadowEntity = new DeviceShadowEntity
                        {
                            DeviceId = deviceId,
                            MsgType = reportType,
                            IP = request.RemoteEndPoint.ToString(),
                            Content = content,
                            CreateTime = DateTime.Now
                        };
                        await _eventPublisher.PublishAsync("Device:Shadow", JSON.Serialize(shadowEntity));
                        return ResponseUtils.Ok();
                    }
                    else
                    {
                        return ResponseUtils.Fail(msg, code);
                    }
                }
                else
                {
                    return ResponseUtils.Fail("路由错误", (int)HttpStatusCode.NotFound);
                }
            }

            return ResponseUtils.Fail("请求方法错误", (int)HttpStatusCode.MethodNotAllowed);
        }

        private async Task ResponseAsync(HttpListenerContext context, object result)
        { 
            var response = context.Response;
            await using Stream outStream = response.OutputStream;
            response.AddHeader("Content-type", "application/json");
            response.ContentEncoding = Encoding.UTF8;
            byte[] b = Encoding.UTF8.GetBytes(JSON.Serialize(result));
            await outStream.WriteAsync(b, 0, b.Length);
        }

        /// <summary>
        /// token检测
        /// </summary>
        /// <param name="request"></param>
        /// <returns></returns>
        private Tuple<bool, int, string> Auth(HttpListenerRequest request)
        {
            var token = request.Headers["Authorization"];
            if (string.IsNullOrWhiteSpace(token))
            {
                return new Tuple<bool, int, string>(false, (int)HttpStatusCode.Unauthorized, "未授权");
            }

            var (isValid, tokenData, validationResult) = JWTEncryption.Validate(token);
            if (!isValid)
            {
                return new Tuple<bool, int, string>(false, (int)HttpStatusCode.Forbidden, "授权错误");
            }

            var expire = Convert.ToDateTime(validationResult.Claims["Expire"]);
            if (expire < DateTime.Now)
            {
                return new Tuple<bool, int, string>(false, (int)HttpStatusCode.Forbidden, "授权过期");
            }

            return new Tuple<bool, int, string>(true, (int)HttpStatusCode.OK, "OK");
        }
    }
}