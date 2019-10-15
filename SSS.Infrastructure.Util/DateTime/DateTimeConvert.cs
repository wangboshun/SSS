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
        public static System.DateTime ConvertIntDateTime(double val)
        {
            System.DateTime time = System.DateTime.MinValue;
            System.DateTime startTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1));
            time = startTime.AddMilliseconds(val);
            return time;
        }

        /// <summary>
        /// Unix时间 转 DateTime 
        /// </summary>
        /// <param name="d"></param>
        /// <returns></returns>
        public static System.DateTime ConvertIntDateTime(string val)
        {
            System.DateTime dateTimeStart = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1));
            long lTime = long.Parse(val + "0000000");
            TimeSpan toNow = new TimeSpan(lTime);
            return dateTimeStart.Add(toNow);
        }

        /// <summary>
        /// DateTime 转 Unix时间
        /// </summary>
        /// <param name="time"></param>
        /// <returns></returns>
        public static long ConvertDateTimeInt(System.DateTime time)
        {
            //double intResult = 0;
            System.DateTime startTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1, 0, 0, 0, 0));
            //intResult = (time- startTime).TotalMilliseconds;
            long t = (time.Ticks - startTime.Ticks) / 10000; //除10000调整为13位
            return t;
        }
    }
}
