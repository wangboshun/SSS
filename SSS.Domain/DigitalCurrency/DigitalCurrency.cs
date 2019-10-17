using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.DigitalCurrency
{
    public class DigitalCurrency : Entity
    {
        /// <summary>
        /// 交易对
        /// </summary>
        public string Coin { set; get; }

        /// <summary>
        /// 交易所
        /// </summary>
        public string Platform { set; get; }

        /// <summary>
        /// 时间段
        /// </summary>
        public string TimeType { set; get; }

        /// <summary>
        /// 开盘价
        /// </summary>
        public double Open { set; get; }

        /// <summary>
        /// 收盘价
        /// </summary>
        public double Close { set; get; }

        /// <summary>
        /// 最高价
        /// </summary>
        public double High { set; get; }

        /// <summary>
        /// 最低价
        /// </summary>
        public double Low { set; get; }

        /// <summary>
        /// 描述
        /// </summary>
        public string Desc { set; get; }

        /// <summary>
        /// 涨跌幅
        /// </summary>
        public double HighRange { set; get; }

        /// <summary>
        /// 当前涨幅
        /// </summary>
        public double CloseRange { set; get; }
    }
}