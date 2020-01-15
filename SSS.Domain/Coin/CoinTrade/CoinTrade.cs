using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Coin.CoinTrade
{
    public class CoinTrade : Entity
    {
        public string Coin { set; get; }
        public string Direction { set; get; }
        public double First_Price { set; get; }
        public double Last_Price { set; get; }

        /// <summary>
        /// 0:sma+macd+kdj 1:sma 2:macd 3:kdj
        /// </summary>
        public int QuantType { set; get; }

        public double Size { set; get; }
        public int Status { set; get; }

        /// <summary>
        /// 1:一分钟 5：五分钟 15：15分钟 60：一小时 240：4小时 1440：一天
        /// </summary>
        public int TimeType { set; get; }

        public string UserId { set; get; }
    }
}