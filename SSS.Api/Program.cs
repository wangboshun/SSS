﻿using Microsoft.AspNetCore;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Server.Kestrel.Core;
using NLog.Web;
using System;
using System.IO;
using System.Net;
using SSS.Infrastructure.Util.Config;

namespace SSS.Api
{
    public class Program
    {
        public static void Main(string[] args)
        {
            CreateWebHostBuilder(args).Build().Run();
        }

        public static IWebHostBuilder CreateWebHostBuilder(string[] args) =>
            WebHost.CreateDefaultBuilder(args)
                .UseUrls("http://*:1234")
                //.UseKestrel(ConfigHttps())
                .UseStartup<Startup>()
                .UseNLog();

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
