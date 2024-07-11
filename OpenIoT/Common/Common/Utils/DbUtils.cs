using Common.Database;
using FreeSql;
using System.Text;

namespace Common.Utils;

public class DbUtils
{
    public static IFreeSql Create(string connectionString, DataType type)
    {
        switch (type)
        {
            case DataType.MySql:
                if (!connectionString.Contains("SslMode", StringComparison.OrdinalIgnoreCase))
                    connectionString += "SslMode=None;";
                if (!connectionString.Contains("Allow User Variables", StringComparison.OrdinalIgnoreCase))
                    connectionString += "Allow User Variables=True;";
                if (!connectionString.Contains("AllowPublicKeyRetrieval", StringComparison.OrdinalIgnoreCase))
                    connectionString += "AllowPublicKeyRetrieval=True;";
                break;
            case DataType.SqlServer:
                if (!connectionString.Contains("Encrypt", StringComparison.OrdinalIgnoreCase))
                    connectionString += "Encrypt=True;";
                if (!connectionString.Contains("TrustServerCertificate", StringComparison.OrdinalIgnoreCase))
                    connectionString += "TrustServerCertificate=True;";
                if (!connectionString.Contains("MultipleActiveResultSets", StringComparison.OrdinalIgnoreCase))
                    connectionString += "MultipleActiveResultSets=False;";
                break;
        }

        var fsql = new FreeSqlBuilder()
            .UseConnectionString(type, connectionString)
            .UseMonitorCommand(x =>
            {
                x.CommandTimeout = 1000;
                Console.WriteLine(x.CommandText);
            }).Build();
        return fsql;
    }

    public static IFreeSql Create(string connectionString, string type)
    {
        var t = (DataType)Enum.Parse(typeof(DataType), type);
        return Create(connectionString, t);
    }

    public static IFreeSql Create(string host, int port, string db, string schema, string user, string password,
        DataType type)
    {
        var connectionString = GetConnectionString(host, port, db, schema, user, password, type);
        return Create(connectionString, type);
    }

    public static IFreeSql Create(string host, int port, string db, string schema, string user, string password,
        string type)
    {
        var t = (DataType)Enum.Parse(typeof(DataType), type);
        var connectionString = GetConnectionString(host, port, db, schema, user, password, t);
        return Create(connectionString, t);
    }

    public static string GetConnectionString(string host, int port, string db, string schema, string user,
        string password,
        DataType type)
    {
        var str = "";
        switch (type)
        {
            case DataType.MySql:
                str =
                    $"Data Source={host};Port={port};User ID={user};Password={password};Initial Catalog={db};Charset=utf8;SslMode=none;Allow User Variables=True;AllowPublicKeyRetrieval=true;AllowLoadLocalInfile=true";
                break;
            case DataType.SqlServer:
                str =
                    $"Data Source={host};Port={port};User Id={user};Password={password};Initial Catalog={db};Encrypt=True;TrustServerCertificate=True;MultipleActiveResultSets=False;";
                break;
            case DataType.PostgreSQL:
                break;
        }

        return str;
    }

    /// <summary>
    ///     获取排序sql
    /// </summary>
    /// <param name="column"></param>
    /// <param name="orderType"></param>
    /// <param name="dbType"></param>
    /// <returns></returns>
    public static string GetOrderBySql(string column, string orderType, DataType dbType)
    {
        return $" ORDER BY {ConvertName(column, dbType)} {orderType} ";
    }

    /// <summary>
    ///     字段转换，不同数据库添加符号
    /// </summary>
    /// <param name="tableName"></param>
    /// <param name="dbType"></param>
    /// <returns></returns>
    public static string ConvertName(string tableName, DataType dbType)
    {
        switch (dbType)
        {
            case DataType.ClickHouse:
            case DataType.MySql:
                return "`" + tableName + "`";
            case DataType.SqlServer:
                return "[" + tableName + "]";
            case DataType.PostgreSQL:
                return "\"" + tableName + "\"";
            case DataType.Dameng:
                return "\"" + tableName + "\"";
            default:
                return tableName;
        }
    }

    /// <summary>
    ///     获取查询sql
    /// </summary>
    /// <param name="tableName"></param>
    /// <param name="columns"></param>
    /// <param name="dbType"></param>
    /// <returns></returns>
    public static string GetQuerySql(string tableName, List<string> columns, DataType dbType)
    {
        var sqlStr = new StringBuilder();
        sqlStr.Append("SELECT  ");
        foreach (var column in columns)
        {
            sqlStr.Append(ConvertName(column, dbType));
            sqlStr.Append(",");
        }

        sqlStr.Remove(sqlStr.Length - 1, 1);
        sqlStr.Append(" FROM ");
        sqlStr.Append(ConvertName(tableName, dbType));
        return sqlStr.ToString();
    }

    public static string GetCountSql(string tableName, DataType dbType)
    {
        var sqlStr = new StringBuilder();
        sqlStr.Append("SELECT COUNT(*) ");
        sqlStr.Append(" FROM ");
        sqlStr.Append(ConvertName(tableName, dbType));
        return sqlStr.ToString();
    }

    /// <summary>
    ///     获取where条件sql和参数
    /// </summary>
    /// <param name="whereList"></param>
    /// <param name="dbType"></param>
    /// <returns></returns>
    public static Tuple<string, Dictionary<string, object>> GetWhereSql(List<WhereInfo> whereList, DataType dbType)
    {
        var param = new Dictionary<string, object>();
        var sqlStr = new StringBuilder();
        var lastStr = " WHERE ";
        sqlStr.Append(lastStr);
        foreach (var item in whereList)
        {
            if (item.Val == null) continue;

            var symbol = item.Symbol.ToLower();

            // in和not in
            if (symbol.Contains("in"))
            {
                sqlStr.Append(ConvertName(item.Column, dbType)).Append(" ");
                //字符串
                if (item.DataType.Equals("string"))
                    sqlStr.Append($" IN ('{string.Join("','", item.Val.ToString().Split(","))}') ");
                //数值
                else
                    sqlStr.Append($" IN ({string.Join(",", item.Val.ToString().Split(","))}) ");
            }
            // length
            else if (symbol.Contains("length"))
            {
                if (item.Symbol.Equals("length>"))
                    sqlStr.Append($" LEN({ConvertName(item.Column, dbType)})>{item.Val}");
                else if (item.Symbol.Equals("length>="))
                    sqlStr.Append($" LEN({ConvertName(item.Column, dbType)})>={item.Val}");
                else if (item.Symbol.Equals("length<"))
                    sqlStr.Append($" LEN({ConvertName(item.Column, dbType)})<{item.Val}");
                else if (item.Symbol.Equals("length<="))
                    sqlStr.Append($" LEN({ConvertName(item.Column, dbType)})<={item.Val}");
            }
            // like
            else if (symbol.Contains("like"))
            {
                sqlStr.Append(ConvertName(item.Column, dbType)).Append(" ");
                sqlStr.Append(item.Symbol).Append($"@{item.Column}");
                param.Add(item.Column, item.Val);
            }
            // is not null
            else if (symbol.Contains("is not null"))
            {
                sqlStr.Append(ConvertName(item.Column, dbType)).Append(" ");
                sqlStr.Append($" IS NOT NULL AND {ConvertName(item.Column, dbType)}<>'' ");
            }
            // is null
            else if (symbol.Contains("is null"))
            {
                sqlStr.Append(ConvertName(item.Column, dbType)).Append(" ");
                sqlStr.Append($" IS NULL OR {ConvertName(item.Column, dbType)}='' ");
            }
            else
            {
                sqlStr.Append(ConvertName(item.Column, dbType)).Append(" ");
                sqlStr.Append(item.Symbol).Append($"@{item.Column}");
                param.Add(item.Column, item.Val);
            }

            lastStr = " " + item.Operate + " ";
            sqlStr.Append(lastStr);
        }

        sqlStr.Remove(sqlStr.Length - lastStr.Length, lastStr.Length - 1);
        return new Tuple<string, Dictionary<string, object>>(sqlStr.ToString(), param);
    }
}