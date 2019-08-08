using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.UserConfig.Dto
{
    public class UserConfigInputDto : InputDtoBase
    {
        public string UserId { set; get; }
        public string coin { set; get; }
        public double size { set; get; }
        public int ktime { set; get; }
        public int profit { set; get; }
        public int loss { set; get; }
        public int status { set; get; }
        public int isdelete { set; get; }
    }
}
