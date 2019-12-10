using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Group.RoleGroup
{
    public class RoleGroup : Entity
    {
        public string ParentId { set; get; }

        public string RoleGroupName { set; get; }
    }
}