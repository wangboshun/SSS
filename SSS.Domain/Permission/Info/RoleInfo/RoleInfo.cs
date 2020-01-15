using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Info.RoleInfo
{
    public class RoleInfo : Entity
    {
        public string ParentId { set; get; }
        public string RoleName { set; get; }
    }
}