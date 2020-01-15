using Microsoft.Extensions.Logging;

using NLog.Extensions.Logging;

namespace SSS.Infrastructure.Util.Log
{
    public static class ApplicationLog
    {
        public static ILogger CreateLogger<T>()
        {
            ILoggerFactory loggerFactory = new LoggerFactory().AddNLog();
            return loggerFactory.CreateLogger<T>();
        }
    }
}