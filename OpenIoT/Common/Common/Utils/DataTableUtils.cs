using System.Data;

namespace Common.Utils;

public class DataTableUtils
{
    /// <summary>
    /// DT转字典
    /// </summary>
    /// <param name="dt"></param>
    /// <returns></returns>
    public static List<Dictionary<string, object>> DataTableToDict(DataTable dt)
    {
        return (from DataRow dr in dt.Rows
                select dt.Columns.Cast<DataColumn>().ToDictionary(x => x.ColumnName, x => dr[x.ColumnName])).ToList();
    }

    /// <summary>
    /// 字典转DT
    /// </summary>
    /// <param name="dict"></param>
    /// <param name="dt"></param>
    /// <returns></returns>
    public static DataRow DictToDataRow(Dictionary<string, object> dict, DataTable dt)
    {
        var row = dt.NewRow();
        foreach (var key in dict.Keys)
            row[key] = dict[key];
        return row;
    }
}