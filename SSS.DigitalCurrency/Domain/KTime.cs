using System;

namespace SSS.DigitalCurrency.Domain
{
    public class KLine
    {
        public int id { set; get; }
        public double amount { set; get; }
        public int count { set; get; }
        public double open { set; get; }
        public double close { set; get; }
        public double low { set; get; }
        public double high { set; get; }
        public double vol { set; get; }
        public DateTime time { set; get; }
    }

    public class AvgPrice
    {
        public DateTime time { set; get; }
        public double price { set; get; }
    }

    public class CoinSymbols
    {
        public string base_currency { set; get; }

        public string quote_currency { set; get; }
    }

    public enum CoinTime
    {
        Time_15min = 1,
        Time_60min = 2,
        Time4_60min = 3,
        Time_1day = 4
    }
}
