using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;

using SSS.Application.Articel.Job;
using SSS.Application.DigitalCurrency.Job;
using SSS.Infrastructure.Seedwork.DbContext;

namespace SSS.WorkService
{
    public class Program
    {
        public static void Main(string[] args)
        {
            CreateHostBuilder(args).Build().Run();
        }

        public static IHostBuilder CreateHostBuilder(string[] args) =>
            Host.CreateDefaultBuilder(args) 
                .ConfigureServices((hostContext, services) =>
                {
                    services.AddScoped<DbcontextBase>(); 
                    services.AddHostedService<ArticelJob>();
                    services.AddHostedService<KLineCaleJob>();
                });
    }
}
