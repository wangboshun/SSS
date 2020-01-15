using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.RoleGroupRelation
{
    public class RoleGroupRelation : Entity
    {
        public string RoleGroupId { set; get; }
        public string RoleId { set; get; }
    }
}