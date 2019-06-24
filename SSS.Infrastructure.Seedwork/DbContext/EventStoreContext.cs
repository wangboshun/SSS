using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Seedwork.DbContext
{
    [DIService(ServiceLifetime.Scoped, typeof(EventStoreContext))]
    public class EventStoreContext : Microsoft.EntityFrameworkCore.DbContext
    {
        public DbSet<EventStore> eventstore { get; set; }
        private readonly IHostingEnvironment _env;

        public EventStoreContext(IHostingEnvironment env)
        {
            _env = env;
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
        }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            var config = new ConfigurationBuilder()
                .SetBasePath(_env.ContentRootPath)
                .AddJsonFile("appsettings.json")
                .Build();

            //optionsBuilder.UseSqlServer(config.GetConnectionString("MSSQLConnection"));
            optionsBuilder.UseMySQL(config.GetConnectionString("MYSQLConnection"));
        }
    }
}
