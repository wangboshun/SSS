using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.UserInfo
{
    public class UserInfo : Entity
    {
        public string UserName { set; get; }

        public string PassWord { set; get; }
    }
}