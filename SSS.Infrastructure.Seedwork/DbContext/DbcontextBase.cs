using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using SSS.Domain.Activity;
using SSS.Domain.Articel;
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

        public DbSet<UserInfo> UserInfo { get; set; }


        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            var config = new ConfigurationBuilder()
                .SetBasePath(_env.ContentRootPath)
                .AddJsonFile("appsettings.json")
                .Build();

            //optionsBuilder.UseSqlServer(config.GetConnectionString("MSSQLConnection"));
            //optionsBuilder.UseMySQL(config.GetConnectionString("MYSQLConnection"));
            optionsBuilder.UseSqlite(config.GetConnectionString("SQLITEConnection"));
        }
    }
}