using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.RoleGroupRelation
{
    public class RoleGroupRelation : Entity
    {
        public string RoleId { set; get; }

        public string RoleGroupId { set; get; }
    }
}