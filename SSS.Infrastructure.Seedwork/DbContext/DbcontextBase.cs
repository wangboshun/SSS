using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;

using SSS.Domain.Activity;
using SSS.Domain.Articel;
using SSS.Domain.CoinInfo;
using SSS.Domain.CoinMessage;
using SSS.Domain.DigitalCurrency;
using SSS.Domain.Trade;
using SSS.Domain.UserActivity;
using SSS.Domain.UserInfo;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Seedwork.DbContext
{
    [DIService(ServiceLifetime.Scoped, typeof(DbcontextBase))]
    public class DbcontextBase : Microsoft.EntityFrameworkCore.DbContext
    {
        private readonly IHostingEnvironment _env;

        public DbcontextBase(IHostingEnvironment env)
        {
            _env = env;
        }

        public DbSet<Activity> Activity { get; set; }

        public DbSet<Articel> Articel { get; set; }

        public DbSet<UserActivity> UserActivity { get; set; }

        public DbSet<DigitalCurrency> DigitalCurrency { get; set; }

        public DbSet<UserInfo> UserInfo { get; set; }

        public DbSet<CoinInfo> CoinInfo { get; set; }

        public DbSet<CoinMessage> CoinMessage { get; set; }

        public DbSet<Trade> Trade { set; get; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            var config = new ConfigurationBuilder()
                .SetBasePath(_env.ContentRootPath)
                .AddJsonFile("appsettings.json")
                .Build();

            //optionsBuilder.UseSqlServer(config.GetConnectionString("MSSQLConnection"));
            optionsBuilder.UseMySql(
                config.GetConnectionString("MYSQLConnection"), 
                builder =>
                {
                    builder.EnableRetryOnFailure(
                        maxRetryCount: 5,
                        maxRetryDelay: TimeSpan.FromSeconds(30),
                        null);
                });
            //optionsBuilder.UseSqlite(config.GetConnectionString("SQLITEConnection"));
        }
    }
}