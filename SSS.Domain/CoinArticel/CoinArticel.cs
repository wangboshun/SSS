using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Coin.CoinArticel
{
    public class CoinArticel : Entity
    {
        public string Title { set; get; }

        public int Sort { set; get; }

        public string Content { set; get; }

        public string Author { set; get; }

        public string Logo { set; get; }

        public int Category { set; get; }
    }
}