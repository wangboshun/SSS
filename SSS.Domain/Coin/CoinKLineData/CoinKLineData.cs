using SSS.Domain.Seedwork.Model;

using System;

namespace SSS.Domain.Coin.CoinKLineData
{
    public class CoinKLineData : Entity
    {


        public string Coin { set; get; }

        public int Timetype { set; get; }

        public double Open { set; get; }

        public double Close { set; get; }

        public double Low { set; get; }

        public double High { set; get; }

        public DateTime Datatime { set; get; }

        public int Platform { set; get; }
    }
}