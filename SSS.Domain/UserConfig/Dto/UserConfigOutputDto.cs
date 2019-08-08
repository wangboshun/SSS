using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.UserConfig.Dto
{
    public class UserConfigOutputDto : OutputDtoBase
    {
        public string Coin { set; get; }

        public int Ktime { set; get; }

        public double Size { set; get; }

        public int Profit { set; get; }

        public int Loss { set; get; }

        public int Status { set; get; }

    }
}
