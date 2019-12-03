using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

using SSS.Domain.Coin.CoinAnalyse;
using SSS.Domain.Coin.CoinArticel;
using SSS.Domain.Coin.CoinInfo;
using SSS.Domain.Coin.CoinMessage;
using SSS.Domain.Coin.CoinTrade;
using SSS.Domain.Permission.Group.UserGroup;
using SSS.Domain.Permission.Info.MenuInfo;
using SSS.Domain.Permission.Info.OperateInfo;
using SSS.Domain.Permission.Info.RoleInfo;
using SSS.Domain.Permission.Info.UserInfo;
using SSS.Domain.Permission.Relation.RoleMenuRelation;
using SSS.Domain.Permission.Relation.RoleOperateRelation;
using SSS.Domain.Permission.Relation.RoleUserRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using SSS.Domain.Permission.Group.PowerGroup;
using SSS.Domain.Permission.Group.RoleGroup;
using SSS.Domain.Permission.Info.PowerInfo;
using SSS.Domain.Permission.Relation.PowerMenuRelation;
using SSS.Domain.Permission.Relation.PowerOperateRelation;
using SSS.Domain.Permission.Relation.RolePowerRelation;
using SSS.Domain.Permission.Relation.RoleUserGroupRelation;
using SSS.Domain.Permission.Relation.UserUserGroupRelation;

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
        public DbSet<MenuInfo> MenuInfo { get; set; }
        public DbSet<OperateInfo> OperateInfo { get; set; }
        public DbSet<PowerInfo> PowerInfo { get; set; }



        public DbSet<RoleUserRelation> RoleUserRelation { get; set; }
        public DbSet<RolePowerRelation> RolePowerRelation { get; set; }
        public DbSet<RoleMenuRelation> RoleMenuRelation { get; set; }
        public DbSet<RoleOperateRelation> RoleOperateRelation { get; set; }
        public DbSet<RoleUserGroupRelation> RoleUserGroupRelation { get; set; }
        public DbSet<PowerOperateRelation> PowerOperateRelation { get; set; }
        public DbSet<PowerMenuRelation> PowerMenuRelation { get; set; }
        public DbSet<UserUserGroupRelation> UserUserGroupRelation { get; set; }





        public DbSet<RoleGroup> RoleGroup { get; set; }
        public DbSet<PowerGroup> PowerGroup { get; set; }
        public DbSet<UserGroup> UserGroup { get; set; }

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