using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.UserInfo
{
    public class UserInfo : Entity
    {
        public string UserName { set; get; }

        public string PassWord { set; get; }

        public string ParentId { set; get; }
    }
}