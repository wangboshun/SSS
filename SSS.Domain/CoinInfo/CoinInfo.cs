using SSS.Domain.Seedwork.Model; 

namespace SSS.Domain.CoinInfo
{
    public class CoinInfo : Entity
    { 

        public string Coin { set; get; }

        public string Name { set; get; }

        public string RomteLogo { set; get; }

        public string LocalLogo { set; get; }

        public string Imagedata { set; get; }

        public string Content { set; get; }
    }
}