using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Info.MenuInfo
{
    public class MenuInfo : Entity
    {
        public string MenuName { set; get; }

        public string ParentId { set; get; }

        public string MenuUrl { set; get; }
    }
}