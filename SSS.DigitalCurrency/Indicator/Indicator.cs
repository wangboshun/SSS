using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

using SSS.DigitalCurrency.Domain;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using System.Linq;

namespace SSS.DigitalCurrency.Indicator
{
    [DIService(ServiceLifetime.Singleton, typeof(Indicator))]
    public class Indicator
    {
        private readonly ILogger _logger;

        public Indicator(ILogger<Indicator> logger)
        {
            _logger = logger;
        }

        /// <summary>
        ///     计算SMA
        /// </summary>
        /// <param name="data">k线</param>
        /// <param name="length">周期</param>
        /// <returns>时间，值，长度</returns>
        public List<Tuple<DateTime, double>> SMA(List<KLine> data, int length)
        {
            List<Tuple<DateTime, double>> result = new List<Tuple<DateTime, double>>();

            for (int i = 0; i < data.Count; i++)
            {
                if (data.Skip(i).Take(length).Count() < length)
                    return result;

                result.Add(new Tuple<DateTime, double>(data[i].time, data.Skip(i).Take(length).Sum(x => x.close) / length));
            }

            return result;
        }

        /// <summary>
        ///     计算EMA
        /// </summary>
        /// <param name="data">k线</param>
        /// <param name="length">周期</param>
        /// <returns></returns>
        public List<Tuple<DateTime, double>> EMA(List<KLine> data, int length)
        {
            List<Tuple<DateTime, double>> result = new List<Tuple<DateTime, double>>();
            try
            {
                if (data.Count < length)
                    return result;

                data = data.OrderBy(x => x.time).ToList();

                double old_ema = data[length - 1].close;

                for (int i = length; i < data.Count; i++)
                {
                    var ema = EmaCale(data[i].close, old_ema, length);
                    old_ema = ema;

                    result.Add(new Tuple<DateTime, double>(data[i].time, old_ema));
                }

                result.Reverse();
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---EMA---");
            }

            return result;
        }

        /// <summary>
        ///     EMA换算   EMA(n)=(2/(N+1))*(C-EMA`)+EMA`   N=长度  C=今日收盘价  EMA`=昨日EMA
        /// </summary>
        /// <param name="close">收盘价</param>
        /// <param name="old_close">上个收盘价</param>
        /// <param name="length">周期</param>
        /// <returns></returns>
        private double EmaCale(double close, double old_close, int length)
        {
            return 2.0 / (length + 1.0) * close + (length - 1.0) / (length + 1.0) * old_close;
        }

        /// <summary>
        ///     ///
        ///     <summary>
        ///         计算MACD
        ///     </summary>
        ///     <param name="data">k线</param>
        ///     <param name="long_">长周期</param>
        ///     <param name="short_">短周期</param>
        ///     <param name="day">周期</param>
        ///     <returns></returns>
        ///     https://blog.csdn.net/smxueer/article/details/52801507  参考资料
        ///     DIF=EMA(12)-EMA(26)
        ///     DEA=(2/(N+1))*DIF+(N-1)/(N+1)*DEA`  N 默认为9
        ///     MACD=2*(DIF-DEA)
        public List<Tuple<DateTime, double, double, double>> MACD(List<KLine> data, int long_ = 26, int short_ = 12,
            int day = 9)
        {
            List<Tuple<DateTime, double, double, double>> result = new List<Tuple<DateTime, double, double, double>>();
            try
            {
                if (data.Count < long_)
                    return result;

                data = data.OrderBy(x => x.time).ToList();

                double old_dea = 0, old_ema_long = data[long_ - 1].close, old_ema_short = data[long_ - 1].close;

                for (int i = long_; i < data.Count; i++)
                {
                    var ema_long = EmaCale(data[i].close, old_ema_long, long_);
                    var ema_short = EmaCale(data[i].close, old_ema_short, short_);

                    var dif = ema_short - ema_long;
                    var dea = (2.0 / (day + 1.0)) * dif + ((day - 1.0) / (day + 1.0)) * old_dea;
                    old_ema_long = ema_long;
                    old_ema_short = ema_short;
                    old_dea = dea;
                    var macd = 2 * (dif - dea);
                    result.Add(new Tuple<DateTime, double, double, double>(data[i].time, dif, dea, macd));
                }

                result.Reverse();
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---MACD---");
            }

            return result;
        }

        /// <summary>
        ///     计算KDJ
        /// </summary>
        /// <param name="data">k线</param>
        /// <param name="n">周期N</param>
        /// <param name="m1">周期M1</param>
        /// <param name="m2">周期M2</param>
        /// <returns></returns>
        public List<Tuple<DateTime, double, double, double>> KDJ(List<KLine> data, int n = 9, int m1 = 3, int m2 = 3)
        {
            List<Tuple<DateTime, double, double, double>> result = new List<Tuple<DateTime, double, double, double>>();
            try
            {
                if (data.Count < n)
                    return result;

                data = data.OrderBy(x => x.time).ToList();
                double old_k = 50, old_d = 50;

                for (int i = n - 1; i < data.Count; i++)
                {
                    double low = data.Skip(i - n + 1).Take(n).Min(x => x.low);
                    double high = data.Skip(i - n + 1).Take(n).Max(x => x.high);

                    var rsv = (data[i].close - low) / (high - low) * 100;

                    var k = (2.0 / 3.0) * old_k + (1.0 / 3.0) * rsv;
                    var d = (2.0 / 3.0) * old_d + (1.0 / 3.0) * k;
                    var j = 3 * k - 2 * d;

                    old_d = d;
                    old_k = k;
                    result.Add(new Tuple<DateTime, double, double, double>(data[i].time, k, d, j));
                }

                result.Reverse();
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---KDJ---");
            }

            return result;
        }

        public List<double> RSI()
        {
            return null;
        }
    }
}