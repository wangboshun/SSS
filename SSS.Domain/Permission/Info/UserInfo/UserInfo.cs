using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Info.UserInfo
{
    public class UserInfo : Entity
    {
        public string ParentId { set; get; }
        public string PassWord { set; get; }
        public string UserName { set; get; }
    }
}