using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Coin.CoinMessage
{
    public class CoinMessage : Entity
    {
        public string Calendar { set; get; }
        public string Coin { set; get; }

        public string Content { set; get; }
        public string Title { set; get; }
    }
}