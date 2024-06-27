using System.Data;

using Microsoft.Data.Analysis;

namespace Common.Utils
{
    public class DataFrameUtils
    {
        public static DataFrame DataTableToDataFrame(DataTable dt)
        {
            var df = FromSchema(dt);
            int count = dt.Columns.Count;
            object[] items = new object[count];
            foreach (DataRow row in dt.Rows)
            {
                for (int i = 0; i < count; i++)
                {
                    if (row[i] == null || row[i] == DBNull.Value)
                    {
                        items[i] = null;
                    }
                    else
                    {
                        items[i] = row[i];
                    }
                }

                df.Append(items, inPlace: true);
            }

            return df;
        }

        private static DataFrame FromSchema(DataTable dt)
        {
            int count = dt.Columns.Count;
            DataFrameColumn[] array = new DataFrameColumn[count];
            for (int i = 0; i < count; i++)
            {
                array[i] = CreateColumn(dt.Columns[i].ColumnName, dt.Columns[i].DataType);
            }

            return new DataFrame(array);
        }

        public static DataFrameColumn CreateColumn(string columnName, Type type, long length = 0L)
        {
            if (type == typeof(int))
            {
                return new Int32DataFrameColumn(columnName, length);
            }

            if (type == typeof(float))
            {
                return new SingleDataFrameColumn(columnName, length);
            }

            if (type == typeof(string))
            {
                return new StringDataFrameColumn(columnName, length);
            }

            if (type == typeof(long))
            {
                return new Int64DataFrameColumn(columnName, length);
            }

            if (type == typeof(decimal))
            {
                return new DecimalDataFrameColumn(columnName, length);
            }

            if (type == typeof(double))
            {
                return new DoubleDataFrameColumn(columnName, length);
            }

            if (type == typeof(DateTime))
            {
                return new DateTimeDataFrameColumn(columnName, length);
            }

            throw new NotSupportedException("不支持的类型");
        }

        public static DataFrameColumn CreateColumn(string columnName, string type, long length = 0L)
        {
            if (type.Equals("int"))
            {
                return new Int32DataFrameColumn(columnName, length);
            }

            if (type.Equals("float"))
            {
                return new SingleDataFrameColumn(columnName, length);
            }

            if (type.Equals("string"))
            {
                return new StringDataFrameColumn(columnName, length);
            }

            if (type.Equals("long"))
            {
                return new Int64DataFrameColumn(columnName, length);
            }

            if (type.Equals("decimal"))
            {
                return new DecimalDataFrameColumn(columnName, length);
            }

            if (type.Equals("double"))
            {
                return new DoubleDataFrameColumn(columnName, length);
            }

            if (type.Equals("DateTime"))
            {
                return new DateTimeDataFrameColumn(columnName, length);
            }

            throw new NotSupportedException("不支持的类型");
        }
    }
}