using System.Collections.Generic;
using System.Linq;

namespace SqlSugar
{
    public class CacheKey
    {
        public string Database { get; set; }
        public List<string> Tables { get; set; }
        public List<string> IdentificationList { get; set; }
        public new string ToString()
        {
            return "SqlSugarDataCache" + UtilConstants.Dot + string.Join(UtilConstants.Dot, this.Tables) + UtilConstants.Dot + string.Join(UtilConstants.Dot, this.IdentificationList.Where(it => it.HasValue()));
        }
    }
}
