using Common.Database;

using FreeRedis;

using FreeSql;

using Furion;

using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

namespace Common.Ext;

public static class ServiceCollectionEx
{
    public static IServiceCollection AddFreeRedis(this IServiceCollection services)
    {
        var url = App.GetConfig<string>("Redis");
        IRedisClient redisClient = new RedisClient(url);
        services.AddSingleton(redisClient);
        return services;
    }

    public static IServiceCollection AddLogStore(this IServiceCollection services)
    {
        Array.ForEach(new[] { LogLevel.Information, LogLevel.Warning, LogLevel.Error }, logLevel =>
        {
            services.AddFileLogging(options =>
            {
                options.Append = true; //追加到已存在日志文件或覆盖它们
                options.DateFormat = "yyyy-MM-dd HH:mm:ss"; //自定义日志输出时间格式
                options.FileNameRule = _ => $"Logs/{DateTime.Now:yyyyMMdd}/{logLevel}-{DateTime.Now:yyyyMMddHH}.log";
                options.WriteFilter = logMsg => logMsg.LogLevel == logLevel;
            });
        });
        return services;
    }

    public static IServiceCollection AddFreeSql(this IServiceCollection services)
    {
        var dataSource = new DynamicDataSource();
        dataSource.DistributeTrace = log => Console.WriteLine(log.Split('\n')[0].Trim());
        var list = App.GetConfig<List<DataSourceEntity>>("DataSource");
        if (list == null)
        {
            return services;
        }

        foreach (var item in list)
        {
            dataSource.Register(item.Module, () =>
            {
                var freeSql = new FreeSqlBuilder()
                    .UseConnectionString(item.Type, item.ConnectString)
                    .UseMonitorCommand(x => { Console.WriteLine($"sql--->{x.CommandText}"); })
                    .Build();

                freeSql.Aop.CommandBefore += (s, e) =>
                {
                    // 如果有意外操作，不执行
                    if (e.Command.CommandText.Contains("truncat") || e.Command.CommandText.Contains("drop"))
                    {
                        e.Command.CommandText = null; //可拦截命令
                    }
                };

                freeSql.Aop.CurdAfter += (s, e) =>
                {

                    if (e.ElapsedMilliseconds > 3000)
                    {
                        Console.WriteLine($"SQL Warning  ElapsedMilliseconds:{e.ElapsedMilliseconds}ms, Sql:{e.Sql}");
                    }
                };
                return freeSql;
            });
        }

        services.AddSingleton<IFreeSql>(dataSource);
        services.AddSingleton(dataSource);
        return services;
    }
}