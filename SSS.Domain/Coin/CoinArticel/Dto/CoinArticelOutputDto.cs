using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Coin.CoinArticel.Dto
{
    public class CoinArticelOutputDto : OutputDtoBase
    {
        public string author { set; get; }
        public string content { set; get; }
        public string logo { set; get; }
        public int sort { set; get; }
        public string title { set; get; }
    }
}