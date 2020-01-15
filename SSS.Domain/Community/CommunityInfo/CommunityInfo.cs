using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Community.CommunityInfo
{
    public class CommunityInfo : Entity
    {
        public string Name { set; get; }

        public string Phone { set; get; }

        public string Wechat { set; get; }

        public string QQ { set; get; }

        public string Email { set; get; }

        public string Detail { set; get; }
    }
}