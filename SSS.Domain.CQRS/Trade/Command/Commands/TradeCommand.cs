using System;

namespace SSS.Domain.CQRS.Trade.Command.Commands
{
    public abstract class TradeCommand : SSS.Domain.Seedwork.Command.Command
    {
        public string id { set; get; }

        public string coin { get; set; }

        public double size { set; get; }

        public double first_price { set; get; }

        public double last_price { set; get; }

        public DateTime last_time { set; get; }

        public DateTime first_time { set; get; }

        public string side { set; get; }

        public string first_trade_no { set; get; }

        public string last_trade_no { set; get; }

        public int first_trade_status { set; get; }

        public int last_trade_status { set; get; }

        public int ktime { set; get; }

        public string userid { set; get; }
    }
}
