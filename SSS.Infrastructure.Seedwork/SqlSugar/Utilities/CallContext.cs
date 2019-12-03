using System.Collections.Generic;
using System.Threading;

namespace SqlSugar
{
    internal class CallContext
    {
        public static ThreadLocal<List<SqlSugarProvider>> ContextList = new ThreadLocal<List<SqlSugarProvider>>();
    }
}
