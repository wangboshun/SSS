using SSS.Domain.Seedwork.Model;
using System;

namespace SSS.Domain.Trade
{
    public class Trade : Entity
    {
        public Trade(string id, string coin, double size, double First_Price, double? Last_Price, string side,
            int First_Trade_Status, int? Last_Trade_Status, string First_Trade_No, string Last_Trade_No,
            DateTime First_Time, DateTime? Last_Time, int KTime)
        {
            this.Id = id;
            this.First_Trade_Status = First_Trade_Status;
            this.Last_Trade_Status = Last_Trade_Status;
            this.Coin = coin;
            this.Size = size;
            this.First_Price = First_Price;
            this.Last_Price = Last_Price;
            this.Side = side;
            this.First_Trade_No = First_Trade_No;
            this.Last_Trade_No = Last_Trade_No;
            this.First_Time = First_Time;
            this.Last_Time = Last_Time;
            this.KTime = KTime;
        }
        public string Coin { set; get; }

        public double Size { set; get; }

        public double First_Price { set; get; }
        public double? Last_Price { set; get; }

        public string Side { set; get; }

        public string First_Trade_No { set; get; }

        public string Last_Trade_No { set; get; }
        public int KTime { set; get; }
        public int First_Trade_Status { set; get; }
        public int? Last_Trade_Status { set; get; }

        public DateTime First_Time { set; get; }
        public DateTime? Last_Time { set; get; }
        public string income { set; get; }
    }
}
