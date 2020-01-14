using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Config;

using System;

namespace SSS.Infrastructure.Seedwork.DbContext
{

    [DIService(ServiceLifetime.Scoped, typeof(DbContextBase))]
    public class DbContextBase : Microsoft.EntityFrameworkCore.DbContext
    {
        private readonly IHostEnvironment _env;
        private readonly ILoggerFactory _factory;

        public DbContextBase(IHostEnvironment env, ILoggerFactory factory)
        {
            _env = env;
            _factory = factory;
        }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            var config = new ConfigurationBuilder()
                .SetBasePath(_env.ContentRootPath)
                .AddJsonFile("appsettings.json")
                .Build();

            var builder = optionsBuilder.UseMySql(config.GetConnectionString("MYSQLConnection"), builder =>
            {
                builder.EnableRetryOnFailure(
                    maxRetryCount: 5,
                    maxRetryDelay: TimeSpan.FromSeconds(30),
                    null);
            });

            var enableLog = JsonConfig.GetSectionValue("SystemConfig:EnableEfSqlLogger");

            if (!string.IsNullOrWhiteSpace(enableLog) && enableLog.Equals("True"))
                builder.UseLoggerFactory(_factory);

            //optionsBuilder.UseSqlite(config.GetConnectionString("SQLITEConnection"));
            //optionsBuilder.UseSqlServer(config.GetConnectionString("MSSQLConnection"));
        }
    }
}
