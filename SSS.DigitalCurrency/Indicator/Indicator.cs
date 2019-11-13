using Microsoft.Extensions.DependencyInjection;

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
        /// <summary>
        /// 计算SMA
        /// </summary>
        /// <param name="close"></param>
        /// <param name="length"></param>
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
        /// 计算EMA
        /// </summary>
        /// <param name="data"></param>
        /// <param name="length"></param>
        /// <returns></returns>
        ///  EMA(n)=(2/(N+1))*(C-EMA`)+EMA`   N=长度  C=今日收盘价  EMA`=昨日EMA
        public List<Tuple<DateTime, double>> EMA(List<KLine> data, int length)
        {
            data.Reverse();

            List<Tuple<DateTime, double>> result = new List<Tuple<DateTime, double>>();

            double old_ema = data[length - 1].close;

            for (int i = length; i < data.Count; i++)
            {
                var ema = EmaCale(data[i].close, old_ema, length);
                old_ema = ema;

                result.Add(new Tuple<DateTime, double>(data[i].time, old_ema));
            }

            //result.Reverse();
            return result;
        }

        /// <summary>
        /// 计算EMA
        /// </summary>
        /// <param name="close">收盘价</param>
        /// <param name="old_close">上个收盘价</param>
        /// <param name="length">时间</param>
        /// <returns></returns>
        private double EmaCale(double close, double old_close, int length)
        {
            return (2.0 / (length + 1.0)) * close + ((length - 1.0) / (length + 1.0)) * old_close;
        }

        /// <summary>
        /// 计算MACD
        /// </summary> 
        /// <param name="length"></param>
        /// <returns></returns>
        /// https://blog.csdn.net/smxueer/article/details/52801507  参考资料
        /// EMA(n)=(2/(N+1))*(C-EMA`)+EMA`   N=长度  C=今日收盘价  EMA`=昨日EMA
        /// DIF=EMA(12)-EMA(26)
        /// DEA=(2/(N+1))*DIF+(N-1)/(N+1)*DEA`  N 默认为9
        /// MACD=2*(DIF-DEA)
        public List<Tuple<DateTime, double, double, double>> MACD(List<KLine> data, int long_ = 26, int short_ = 12, int day = 9)
        {
            data.Reverse();

            List<Tuple<DateTime, double, double, double>> result = new List<Tuple<DateTime, double, double, double>>();

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
            //result.Reverse();
            return result;
        }

        public List<double> KDJ()
        {
            return null;
        }

        public List<double> RSI()
        {
            return null;
        }
    }
}
