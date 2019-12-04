using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.RoleRoleGroupRelation
{
    public class RoleRoleGroupRelation : Entity
    {
        public string RoleId { set; get; }

        public string RoleGroupId { set; get; }
    }
}