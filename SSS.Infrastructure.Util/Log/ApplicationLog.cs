using Microsoft.Extensions.Logging;

namespace SSS.Infrastructure.Util.Log
{
    public static class ApplicationLog
    {
        public static ILoggerFactory LoggerFactory { get; } = new LoggerFactory();

        public static ILogger CreateLogger<T>()
        {
            return LoggerFactory.CreateLogger<T>();
        }
    }
}