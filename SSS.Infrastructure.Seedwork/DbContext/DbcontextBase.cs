using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

using SSS.Domain.Activity;
using SSS.Domain.Coin.CoinArticel;
using SSS.Domain.Coin.CoinAnalyse;
using SSS.Domain.Coin.CoinInfo;
using SSS.Domain.Coin.CoinMessage;
using SSS.Domain.Coin.CoinTrade;
using SSS.Domain.Permission.MenuInfo;
using SSS.Domain.Permission.OperateInfo;
using SSS.Domain.Permission.RoleInfo;
using SSS.Domain.Permission.RoleMenu;
using SSS.Domain.Permission.RoleOperate;
using SSS.Domain.Permission.UserInfo;
using SSS.Domain.Permission.UserRole;
using SSS.Domain.UserActivity;
using SSS.Infrastructure.Util.Attribute;

using System;

namespace SSS.Infrastructure.Seedwork.DbContext
{
    [DIService(ServiceLifetime.Scoped, typeof(DbcontextBase))]
    public class DbcontextBase : Microsoft.EntityFrameworkCore.DbContext
    {
        private readonly IHostEnvironment _env;
        private readonly ILoggerFactory _factory;

        public DbcontextBase(IHostEnvironment env, ILoggerFactory factory)
        {
            _env = env;
            _factory = factory;
        }

        public DbSet<Activity> Activity { get; set; }

        public DbSet<UserActivity> UserActivity { get; set; }

        #region Coin

        public DbSet<CoinArticel> CoinArticel { get; set; }

        public DbSet<CoinAnalyse> CoinAnalyse { get; set; }

        public DbSet<CoinInfo> CoinInfo { get; set; }

        public DbSet<CoinMessage> CoinMessage { get; set; }

        public DbSet<CoinTrade> CoinTrade { set; get; }

        #endregion


        #region Permission

        public DbSet<UserInfo> UserInfo { get; set; }

        public DbSet<RoleInfo> RoleInfo { get; set; }

        public DbSet<UserRole> UserRole { get; set; }

        public DbSet<MenuInfo> MenuInfo { get; set; }

        public DbSet<RoleMenu> RoleMenu { get; set; }

        public DbSet<OperateInfo> OperateInfo { get; set; }

        public DbSet<RoleOperate> RoleOperate { get; set; }

        #endregion

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            var config = new ConfigurationBuilder()
                .SetBasePath(_env.ContentRootPath)
                .AddJsonFile("appsettings.json")
                .Build();


            optionsBuilder.UseMySql(
                config.GetConnectionString("MYSQLConnection"),
                builder =>
                {
                    builder.EnableRetryOnFailure(
                        maxRetryCount: 5,
                        maxRetryDelay: TimeSpan.FromSeconds(30),
                        null);
                }).UseLoggerFactory(_factory);

            //optionsBuilder.UseSqlite(config.GetConnectionString("SQLITEConnection"));
            //optionsBuilder.UseSqlServer(config.GetConnectionString("MSSQLConnection"));
        }
    }
}