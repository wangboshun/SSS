using Autofac.Extensions.DependencyInjection;

using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Server.Kestrel.Core;
using Microsoft.Extensions.Hosting;

using NLog.Web;

using SSS.Infrastructure.Util.Config;

using System;
using System.IO;
using System.Net;

namespace SSS.Api
{
    /// <summary>
    ///     Program
    /// </summary>
    public class Program
    {
        /// <summary>
        ///     Main
        /// </summary>
        /// <param name="args"></param>
        public static void Main(string[] args)
        {
            CreateHostBuilder(args).Build().Run();
        }

        /// <summary>
        ///     CreateHostBuilder
        /// </summary>
        /// <param name="args"></param>
        /// <returns></returns>
        public static IHostBuilder CreateHostBuilder(string[] args)
        {
            return Host.CreateDefaultBuilder(args)
                .UseServiceProviderFactory(new AutofacServiceProviderFactory())
                .ConfigureWebHostDefaults(web =>
                {
                    //web.UseKestrel(ConfigHttps());
                    web.UseUrls("http://*:81");
                    web.UseStartup<Startup>();
                })
                .UseNLog();
        }

        /// <summary>
        ///     ConfigHttps
        /// </summary>
        /// <returns></returns>
        private static Action<KestrelServerOptions> ConfigHttps()
        {
            return x =>
            {
                x.Listen(IPAddress.Loopback, 443, listenOptions =>
                {
                    var path = Directory.GetCurrentDirectory() + "\\File\\cert\\" + JsonConfig.GetSectionValue("Cert:Path");
                    listenOptions.UseHttps(path, JsonConfig.GetSectionValue("Cert:PassWord"));
                });
            };
        }
    }
}