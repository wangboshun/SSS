using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.UserActivity
{
    public class UserActivity : Entity
    {
        public string ActivityId { set; get; }

        public string UserId { set; get; }

        public string WechatName { set; get; }

        public int GroupNumber { set; get; }

        public int Status { set; get; }
    }
}