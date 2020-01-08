using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Community.CommunityInfo.Dto
{
    public class CommunityInfoInputDto : InputDtoBase
    {
        public string userid { set; get; }
        public string name { set; get; }
        public string qq { set; get; }
        public string phone { set; get; }
        public string wechat { set; get; }
        public string email { set; get; }
        public string detail { set; get; }
    }
}
