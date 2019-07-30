using SSS.Domain.Seedwork.Model;
using System;

namespace SSS.Domain.Trade.Dto
{
    public class TradeInputDto : InputDtoBase
    {
        public string coin { set; get; }

        public double size { set; get; }

        public double last_price { set; get; }
        public double first_price { set; get; }
        public DateTime last_time { set; get; }
        public DateTime first_time { set; get; }

        public string side { set; get; }

        //("-2":失败,"-1":撤单成功,"0":等待成交 ,"1":部分成交, "2":完全成交,"3":下单中,"4":撤单中）
        public int first_trade_status { set; get; }

        public int last_trade_status { set; get; }

        public string first_trade_no { set; get; }

        public string last_trade_no { set; get; }

        public int ktime { set; get; }

        public string income { set; get; }

        public string userid { set; get; }
    }
}
