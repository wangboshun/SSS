using SSS.Domain.Seedwork.Model;


namespace SSS.Domain.Coin.CoinMessage.Dto
{
    public class CoinMessageOutputDto : OutputDtoBase
    {
        public string Coin { set; get; }

        public string Title { set; get; }

        public string Content { set; get; }

        public string Calendar { set; get; }
        public string Logo { set; get; }
    }
}
