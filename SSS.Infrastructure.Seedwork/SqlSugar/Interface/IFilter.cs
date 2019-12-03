using System.Collections.Generic;

namespace SqlSugar
{
    public interface IFilter
    {
        IFilter Add(SqlFilterItem filter);
        void Remove(string filterName);
        List<SqlFilterItem> GeFilterList { get; }
    }
}
