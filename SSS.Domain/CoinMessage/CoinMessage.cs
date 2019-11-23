using SSS.Domain.Seedwork.Model;


namespace SSS.Domain.CoinMessage
{
    public class CoinMessage : Entity
    {
        public string Coin { set; get; }

        public string Title { set; get; }

        public string Content { set; get; }

        public string Calendar { set; get; }
    }
}