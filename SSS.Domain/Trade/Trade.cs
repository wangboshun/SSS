using SSS.Domain.Seedwork.Model;

using System;

namespace SSS.Domain.Trade
{
    public class Trade : Entity
    {
        public string UserId { set; get; }

        public string Coin { set; get; }

        public double First_Price { set; get; }

        public double Last_Price { set; get; }

        public double Size { set; get; }

        public string Direction { set; get; }

        public int Status { set; get; }

        public DateTime? UpdateTime { set; get; }

    }
}