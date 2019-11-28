using System;

namespace SSS.Infrastructure.Util.DateTime
{
    public static class DateTimeConvert
    {
        /// <summary>
        ///     Unix时间 转 DateTime
        /// </summary>
        /// <param name="d"></param>
        /// <returns></returns>
        public static System.DateTime ConvertIntDateTime(double val)
        {
            //System.DateTime time = System.DateTime.MinValue;
            //System.DateTime startTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1));
            //time = startTime.AddMilliseconds(val);
            //return time; 

            System.DateTime dateTimeStart = TimeZoneInfo.ConvertTimeToUtc(new System.DateTime(1970, 1, 1));
            long lTime = long.Parse(val + "0000000");
            TimeSpan toNow = new TimeSpan(lTime);
            return dateTimeStart.Add(toNow);
        }

        /// <summary>
        ///     Unix时间 转 DateTime
        /// </summary>
        /// <param name="d"></param>
        /// <returns></returns>
        public static System.DateTime ConvertDateTime(string val)
        {
            System.DateTime dateTimeStart = TimeZoneInfo.ConvertTimeToUtc(new System.DateTime(1970, 1, 1));
            long lTime = long.Parse(val + "0000000");
            TimeSpan toNow = new TimeSpan(lTime);
            return dateTimeStart.Add(toNow);
        }

        /// <summary>
        ///     Unix时间 转 时间字符串  yyyy-MM-dd HH:mm:ss
        /// </summary>
        /// <param name="val"></param>
        /// <param name="format">yyyy-MM-dd HH:mm:ss</param>
        /// <returns></returns>
        public static string ConvertIntDateTimeToString(string val, string format = "yyyy-MM-dd HH:mm:ss")
        {
            System.DateTime dateTimeStart = TimeZoneInfo.ConvertTimeToUtc(new System.DateTime(1970, 1, 1));
            long lTime = long.Parse(val + "0000000");
            TimeSpan toNow = new TimeSpan(lTime);
            System.DateTime now = dateTimeStart.Add(toNow);
            return now.ToString(format);
        }

        /// <summary>
        ///     DateTime 转 时间字符串  yyyy-MM-dd HH:mm:ss
        /// </summary>
        /// <param name="time"></param>
        /// <param name="format">yyyy-MM-dd HH:mm:ss</param>
        /// <returns></returns>
        public static string ConvertDateTimeToString(System.DateTime time, string format = "yyyy-MM-dd HH:mm:ss")
        {
            return time.ToString(format);
        }

        /// <summary>
        ///     DateTime 转 时间字符串  yyyy-MM-dd HH:mm:ss
        /// </summary>
        /// <param name="time"></param>
        /// <param name="format">yyyy-MM-dd HH:mm:ss</param>
        /// <returns></returns>
        public static string ConvertDateTimeToStr(this System.DateTime time, string format = "yyyy-MM-dd HH:mm:ss")
        {
            return time.ToString(format);
        }

        /// <summary>
        ///     DateTime 转 Unix时间
        /// </summary>
        /// <param name="time"></param>
        /// <returns></returns>
        public static long ConvertDateTimeInt(System.DateTime time)
        {
            //double intResult = 0;
            System.DateTime startTime = TimeZoneInfo.ConvertTimeToUtc(new System.DateTime(1970, 1, 1, 0, 0, 0, 0));
            //intResult = (time- startTime).TotalMilliseconds;
            long t = (time.Ticks - startTime.Ticks) / 10000; //除10000调整为13位
            return t;
        }
    }
}