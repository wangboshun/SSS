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
        /// 1:һ���� 5������� 15��15���� 60��һСʱ 240��4Сʱ 1440��һ��
        /// </summary>
        public int TimeType { set; get; }

        public string UserId { set; get; }
    }
}