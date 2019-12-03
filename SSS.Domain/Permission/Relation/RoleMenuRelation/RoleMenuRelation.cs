using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.RoleMenuRelation
{
    public class RoleMenuRelation : Entity
    {
        public string MenuId { set; get; }

        public string RoleId { set; get; }
    }
}