using FreeSql;

namespace Common.Database;

public class DataSourceEntity
{
    public string Module { set; get; }
    public string ConnectString { set; get; }
    public DataType Type { set; get; }
}