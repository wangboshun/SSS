using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

using Newtonsoft.Json;

using Quartz;

using SSS.Application.Job.JobSetting.Extension;
using SSS.Domain.Coin.CoinInfo;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using System.Linq;
using System.Net;
using System.Runtime.InteropServices;
using System.Threading.Tasks;

namespace SSS.Application.Job.Coin.CoinInfo
{
    [DIService(ServiceLifetime.Transient, typeof(CoinInfoJob))]
    public class CoinInfoJob : IJob
    {
        private readonly IHostEnvironment _env;
        private readonly ILogger _logger;
        private readonly IServiceScopeFactory _scopeFactory;
        private static readonly object _lock = new object();

        public CoinInfoJob(ILogger<CoinInfoJob> logger, IServiceScopeFactory scopeFactory, IHostEnvironment env)
        {
            _logger = logger;
            _env = env;
            _scopeFactory = scopeFactory;
        }

        public Task Execute(IJobExecutionContext context)
        {
            _logger.LogInformation("-----------------CoinInfoJob----------------------");
            return DoWork(context);
        }

        public Task DoWork(IJobExecutionContext context)
        {
            lock (_lock)
            {
                var trigger = (Quartz.Impl.Triggers.CronTriggerImpl)((Quartz.Impl.JobExecutionContextImpl)context).Trigger;
                try
                {
                    Stopwatch watch = new Stopwatch();
                    watch.Start();

                    GetCoinInfo();

                    watch.Stop();
                    context.Scheduler.Context.Put(trigger.FullName + "_Result", "Success");
                    context.Scheduler.Context.Put(trigger.FullName + "_Time", watch.ElapsedMilliseconds);

                    _logger.LogInformation($"------>{context.GetJobDetail()}  耗时：{watch.ElapsedMilliseconds} ");
                    return Task.FromResult("Success");
                }
                catch (Exception ex)
                {
                    context.Scheduler.Context.Put(trigger.FullName + "_Exception", ex);
                    _logger.LogError(new EventId(ex.HResult), ex, "---CoinInfoJob DoWork Exception---");
                    return Task.FromResult("Error");
                }
            }
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
                var data = JsonConvert.DeserializeObject<List<CoinJson>>(json);

                using var scope = _scopeFactory.CreateScope();
                using var context = scope.ServiceProvider.GetRequiredService<CoinDbContext>();
                var source = context.CoinInfo.ToList();

                var list = new List<Domain.Coin.CoinInfo.CoinInfo>();

                Parallel.ForEach(data, (item) =>
                {
                    if (source.Any(x => x.Name.Equals(item.name)))
                        return;

                    if (list.Any(x => x.Name.Equals(item.name)))
                        return;

                    var model = new Domain.Coin.CoinInfo.CoinInfo
                    {
                        Content = item.id,
                        Coin = item.symbol,
                        RomteLogo = item.logo_png,
                        LocalLogo = DownLoadCoinLogo(item.symbol, item.logo_png),
                        Imagedata = UrlToBase64(item.logo_png),
                        Id = Guid.NewGuid().ToString(),
                        Name = item.name,
                        CreateTime = DateTime.Now
                    };

                    list.Add(model);
                });

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
                return "https://s1.bqiapp.com/coin/20181030_72_png/bitcoin_200_200.png?v=1566978037";
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
                byte[] bytes = web.DownloadData(url);
                using MemoryStream ms = new MemoryStream(bytes);
                Image img = Image.FromStream(ms);
                Bitmap bmp = new Bitmap(img);
                using MemoryStream stream = new MemoryStream();
                bmp.Save(stream, ImageFormat.Jpeg);
                byte[] arr = new byte[stream.Length];
                stream.Position = 0;
                stream.Read(arr, 0, (int)stream.Length);
                stream.Close();
                return Convert.ToBase64String(arr);
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---UrlToBase64 Exception---");
                return "";
            }
        }
    }
}