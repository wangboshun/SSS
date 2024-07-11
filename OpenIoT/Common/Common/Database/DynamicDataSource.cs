using Common.Utils;

using FreeSql;

namespace Common.Database;

/// <summary>
///     动态数据源
/// </summary>
public class DynamicDataSource : FreeSqlCloud<string>
{
    public DynamicDataSource() : base(null)
    {
    }

    public void AddDataSource(string key, string host, int port, string db, string schema, string user,
        string password, DataType type)
    {
        var connectionString = DbUtils.GetConnectionString(host, port, db, schema, user, password, type);
        Register(key, () =>
        {
            var freeSql = new FreeSqlBuilder()
                .UseConnectionString(type, connectionString)
                .UseMonitorCommand(x => { Console.WriteLine($"sql--->{x.CommandText}"); })
                .Build();
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
}