using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.RoleInfo
{
    public class RoleInfo : Entity
    {
        public string RoleName { set; get; }

        public string ParentId { set; get; }
    }
}