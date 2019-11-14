using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using System.Linq;
using System.Net;
using System.Runtime.InteropServices;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using SSS.Domain.CoinInfo;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Application.CoinInfo.Job
{
    [DIService(ServiceLifetime.Transient, typeof(IHostedService))]
    public class CoinInfoJob : IHostedService, IDisposable
    {
        private readonly IHostEnvironment _env;
        private readonly ILogger _logger;
        private readonly IServiceScopeFactory _scopeFactory;
        private Timer _timer;

        public CoinInfoJob(ILogger<CoinInfoJob> logger, IServiceScopeFactory scopeFactory, IHostEnvironment env)
        {
            _logger = logger;
            _env = env;
            _scopeFactory = scopeFactory;
        }

        public void Dispose()
        {
            _timer?.Dispose();
        }

        public Task StartAsync(CancellationToken stoppingToken)
        {
            _timer = new Timer(DoWork, null, TimeSpan.Zero,
                TimeSpan.FromMinutes(60));

            return Task.CompletedTask;
        }

        public Task StopAsync(CancellationToken stoppingToken)
        {
            _timer?.Change(Timeout.Infinite, 0);

            return Task.CompletedTask;
        }

        private void DoWork(object state)
        {
            GetCoinInfo();
        }

        /// <summary>
        ///     获取币币信息
        /// </summary>
        public void GetCoinInfo()
        {
            try
            {
                WebClient web = new WebClient();
                var json = web.DownloadString("https://fxhapi.feixiaohao.com/public/v1/ticker?start=0&limit=10000");
                List<CoinJson> data = JsonConvert.DeserializeObject<List<CoinJson>>(json);

                using var scope = _scopeFactory.CreateScope();
                using var context = scope.ServiceProvider.GetRequiredService<DbcontextBase>();
                var source = context.CoinInfo.ToList();

                List<Domain.CoinInfo.CoinInfo> list = new List<Domain.CoinInfo.CoinInfo>();

                foreach (var item in data)
                {
                    Domain.CoinInfo.CoinInfo model = new Domain.CoinInfo.CoinInfo();
                    model.Content = item.id;
                    model.Coin = item.symbol;

                    if (source.Any(x => x.Coin.Equals(model.Coin) && !string.IsNullOrWhiteSpace(x.Imagedata)))
                        continue;

                    if (list.Any(x => x.Coin.Equals(model.Coin) && !string.IsNullOrWhiteSpace(x.Imagedata)))
                        continue;

                    string src = item.logo_png;
                    model.RomteLogo = src;
                    model.LocalLogo = DownLoadCoinLogo(model.Coin, src);
                    model.Imagedata = UrlToBase64(src);
                    model.Id = Guid.NewGuid().ToString();
                    model.Name = item.name;
                    model.CreateTime = DateTime.Now;
                    list.Add(model);
                }

                context.CoinInfo.AddRange(list);
                context.SaveChanges();
                Console.WriteLine("---GetCoinInfo  SaveChanges---");
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---GetCoinInfo Exception---");
            }
        }

        /// <summary>
        ///     下载币币Logo
        /// </summary>
        /// <param name="coin"></param>
        /// <param name="url"></param>
        private string DownLoadCoinLogo(string coin, string url)
        {
            try
            {
                WebClient web = new WebClient();
                string filepath = RuntimeInformation.IsOSPlatform(OSPlatform.Linux)
                    ? _env.ContentRootPath + "//File//coin//"
                    : _env.ContentRootPath + "\\File\\coin\\";
                string filename = filepath + coin + ".png";

                if (!File.Exists(filename))
                    web.DownloadFile(url, filename);

                return "/File/coin/" + coin + ".png";
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, $"---{coin} DownLoadCoinLogo Exception---");
                return "";
            }
        }

        /// <summary>
        ///     币币Logo转换为Base64
        /// </summary>
        /// <param name="url"></param>
        /// <returns></returns>
        private string UrlToBase64(string url)
        {
            try
            {
                WebClient web = new WebClient();
                byte[] Bytes = web.DownloadData(url);
                using (MemoryStream ms = new MemoryStream(Bytes))
                {
                    Image img = Image.FromStream(ms);
                    Bitmap bmp = new Bitmap(img);
                    using (MemoryStream stream = new MemoryStream())
                    {
                        bmp.Save(stream, ImageFormat.Jpeg);
                        byte[] arr = new byte[stream.Length];
                        stream.Position = 0;
                        stream.Read(arr, 0, (int)stream.Length);
                        stream.Close();
                        return Convert.ToBase64String(arr);
                    }
                }
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---UrlToBase64 Exception---");
                return "";
            }
        }
    }
}