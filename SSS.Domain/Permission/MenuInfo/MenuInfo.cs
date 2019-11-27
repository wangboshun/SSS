using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.MenuInfo
{
    public class MenuInfo : Entity
    {
        public string MenuName { set; get; }

        public string ParentId { set; get; }
    }
}