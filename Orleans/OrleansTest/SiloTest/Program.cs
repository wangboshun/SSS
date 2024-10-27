using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.DependencyInjection;

namespace SiloTest
{
    internal class Program
    {
        static async Task Main(string[] args)
        {
            IHostBuilder builder = Host.CreateDefaultBuilder(args)
     .UseOrleans(silo =>
     {
         silo.UseLocalhostClustering()
             .ConfigureLogging(logging => logging.AddConsole());
     })
     .UseConsoleLifetime();

            using IHost host = builder.Build();

            await host.RunAsync();
        }
    }
}
