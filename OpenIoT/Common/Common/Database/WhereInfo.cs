namespace Common.Database;

public class WhereInfo
{
    /// <summary>
    ///     字段
    /// </summary>
    public string Column { set; get; }

    /// <summary>
    ///     运算符：=、>、<、IN、 LIKE、 IS NULL
    /// </summary>
    public string Symbol { set; get; }

    /// <summary>
    ///     数据类型
    /// </summary>
    public string DataType { set; get; }

    /// <summary>
    ///     值
    /// </summary>
    public dynamic Val { set; get; }

    /// <summary>
    ///     操作符：AND、OR
    /// </summary>
    public string Operate { set; get; }
}