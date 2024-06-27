using System.Globalization;

namespace Common.Utils;

public class TimeUtils
{
    private static readonly long START_TIMESTAMP = 621355968000000000;

    /// <summary>
    ///     时间戳转日期时间
    /// </summary>
    /// <param name="timestamp"></param>
    /// <param name="hasMilliseconds">是否包含毫秒</param>
    /// <returns></returns>
    public static DateTime TimestampToDateTime(long timestamp, bool hasMilliseconds = false)
    {
        return hasMilliseconds
            ? new DateTime(START_TIMESTAMP + timestamp * 10000, DateTimeKind.Utc).ToLocalTime()
            : new DateTime(START_TIMESTAMP + timestamp * 10000000, DateTimeKind.Utc).ToLocalTime();
    }

    /// <summary>
    ///     日期时间转时间戳
    /// </summary>
    /// <param name="time"></param>
    /// <param name="hasMilliseconds">是否包含毫秒</param>
    /// <returns></returns>
    public static long DateTimeToTimestamp(DateTime time, bool hasMilliseconds = false)
    {
        return hasMilliseconds
            ? (time.ToUniversalTime().Ticks - START_TIMESTAMP) / 10000
            : (time.ToUniversalTime().Ticks - START_TIMESTAMP) / 10000000;
    }

    /// <summary>
    ///     字符串转日期时间
    /// </summary>
    /// <param name="str"></param>
    /// <param name="format"></param>
    /// <returns></returns>
    public static DateTime StringToDateTime(string str, string format = "yyyy-MM-dd HH:mm:ss")
    {
        return DateTime.ParseExact(str, format, CultureInfo.CurrentCulture);
    }

    /// <summary>
    ///     日期时间转字符串
    /// </summary>
    /// <param name="time"></param>
    /// <param name="format"></param>
    /// <returns></returns>
    public static string DateTimeToString(DateTime time, string format = "yyyy-MM-dd HH:mm:ss")
    {
        return Convert.ToDateTime(time).ToString(format);
    }

    /// <summary>
    /// 获取时间间隔，字符串由数字和单位组成，如5second,1day等,支持时、分、秒、天
    /// </summary>
    /// <param name="time"></param>
    /// <returns></returns>
    public static TimeSpan GetTimeSpan(string time)
    {
        TimeSpan timeSpan = new TimeSpan();
        if (time.Contains("second"))
        {
            timeSpan = TimeSpan.FromSeconds(Convert.ToInt32(time.Replace("second", "")));
        }
        else if (time.Contains("minute"))
        {
            timeSpan = TimeSpan.FromMinutes(Convert.ToInt32(time.Replace("minute", "")));
        }
        else if (time.Contains("hour"))
        {
            timeSpan = TimeSpan.FromHours(Convert.ToInt32(time.Replace("hour", "")));
        }
        else if (time.Contains("day"))
        {
            timeSpan = TimeSpan.FromDays(Convert.ToInt32(time.Replace("day", "")));
        }
        return timeSpan;
    }
}