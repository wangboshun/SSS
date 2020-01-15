using SSS.Domain.Seedwork.Model;

using System;

namespace SSS.Domain.Coin.CoinKLineData
{
    public class CoinKLineData : Entity
    {
        public double Close { set; get; }
        public string Coin { set; get; }

        public DateTime DataTime { set; get; }
        public double High { set; get; }
        public double Low { set; get; }
        public double Open { set; get; }
        public int Platform { set; get; }
        public int TimeType { set; get; }
    }
}