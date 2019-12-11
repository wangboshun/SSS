using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Coin.CoinTrade.Dto
{
    public class CoinTradeOutputDto : OutputDtoBase
    {
        public string coin { set; get; }
        public string first_price { set; get; }
        public string last_price { set; get; }
        public string direction { set; get; }
        public string updatetime { set; get; }
    }
}