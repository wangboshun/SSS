using Microsoft.AspNetCore;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Server.Kestrel.Core;

using NLog.Web;

using SSS.Infrastructure.Util.Config;

using System;
using System.IO;
using System.Net;

namespace SSS.Api
{
    /// <summary>
    /// Program 
    /// </summary>
    public class Program
    {
        /// <summary>
        /// Main
        /// </summary>
        /// <param name="args"></param>
        public static void Main(string[] args)
        {
            CreateWebHostBuilder(args).Build().Run();
        }

        /// <summary>
        /// CreateWebHostBuilder
        /// </summary>
        /// <param name="args"></param>
        /// <returns></returns>
        public static IWebHostBuilder CreateWebHostBuilder(string[] args)
        {
            return WebHost.CreateDefaultBuilder(args)
                .UseUrls("http://*:81")
                //.UseKestrel(ConfigHttps())
                .UseStartup<Startup>()
                .UseNLog();
        }

        private static Action<KestrelServerOptions> ConfigHttps()
        {
            return x =>
            {
                x.Listen(IPAddress.Loopback, 443, listenOptions =>
                {
                    var path = Directory.GetCurrentDirectory() + "\\File\\cert\\" + Config.GetSectionValue("Cert:Path");
                    listenOptions.UseHttps(path, Config.GetSectionValue("Cert:PassWord"));
                });
            };
        }
    }
}