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

    /// <summary>
    ///     时间线  分钟为单位
    /// </summary>
    public enum CoinTime
    {
        Time_1min = 1,
        Time_5min = 5,
        Time_15min = 15,
        Time_60min = 60,
        Time_4hour = 240,
        Time_1day = 1440
    }

    public enum Platform
    {
        Huobi = 1,
        Okex = 2
    }

    public enum QuantEnum
    {
        Macd_Sma_Kdj = 0,
        Sma = 1,
        Macd = 2,
        Kdj = 3
    }
}