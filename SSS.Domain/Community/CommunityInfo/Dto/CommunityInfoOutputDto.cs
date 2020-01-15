using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Community.CommunityInfo.Dto
{
    public class CommunityInfoOutputDto : OutputDtoBase
    {
        public string Name { set; get; }
        public string Phone { set; get; }
        public string WeChat { set; get; }
        public string QQ { set; get; }
        public string Email { set; get; }
        public string Detail { set; get; }
    }
}