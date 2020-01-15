using System;

namespace SSS.Infrastructure.Util.DateTime
{
    public static class DateTimeConvert
    {
        /// <summary>
        /// Unix时间 转 DateTime
        /// </summary>
        /// <param name="d"></param>
        /// <returns></returns>
        public static System.DateTime ConvertDateTime(string val)
        {
            var dtStart = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1));
            var lTime = long.Parse(val + "0000000");
            var toNow = new TimeSpan(lTime);
            return dtStart.Add(toNow);
        }

        /// <summary>
        /// DateTime 转 Unix时间
        /// </summary>
        /// <param name="time"></param>
        /// <returns></returns>
        public static long ConvertDateTimeInt(System.DateTime time)
        {
            //double intResult = 0;
            var startTime = TimeZoneInfo.ConvertTimeToUtc(new System.DateTime(1970, 1, 1, 0, 0, 0, 0));
            //intResult = (time- startTime).TotalMilliseconds;
            var t = (time.Ticks - startTime.Ticks) / 10000; //除10000调整为13位
            return t;
        }

        /// <summary>
        /// DateTime 转 时间字符串 yyyy-MM-dd HH:mm:ss
        /// </summary>
        /// <param name="time"></param>
        /// <param name="format">yyyy-MM-dd HH:mm:ss</param>
        /// <returns></returns>
        public static string ConvertDateTimeToStr(this System.DateTime time, string format = "yyyy-MM-dd HH:mm:ss")
        {
            return time.ToString(format);
        }

        /// <summary>
        /// DateTime 转 时间字符串 yyyy-MM-dd HH:mm:ss
        /// </summary>
        /// <param name="time"></param>
        /// <param name="format">yyyy-MM-dd HH:mm:ss</param>
        /// <returns></returns>
        public static string ConvertDateTimeToString(System.DateTime time, string format = "yyyy-MM-dd HH:mm:ss")
        {
            return time.ToString(format);
        }

        /// <summary>
        /// Unix时间 转 DateTime
        /// </summary>
        /// <param name="d"></param>
        /// <returns></returns>
        public static System.DateTime ConvertIntDateTime(double val)
        {
            var dtStart = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1));
            var lTime = long.Parse(val + "0000000");
            var toNow = new TimeSpan(lTime);
            return dtStart.Add(toNow);
        }

        /// <summary>
        /// Unix时间 转 时间字符串 yyyy-MM-dd HH:mm:ss
        /// </summary>
        /// <param name="val"></param>
        /// <param name="format">yyyy-MM-dd HH:mm:ss</param>
        /// <returns></returns>
        public static string ConvertIntDateTimeToString(string val, string format = "yyyy-MM-dd HH:mm:ss")
        {
            var dateTimeStart = TimeZoneInfo.ConvertTimeToUtc(new System.DateTime(1970, 1, 1));
            var lTime = long.Parse(val + "0000000");
            var toNow = new TimeSpan(lTime);
            var now = dateTimeStart.Add(toNow);
            return now.ToString(format);
        }
    }
}