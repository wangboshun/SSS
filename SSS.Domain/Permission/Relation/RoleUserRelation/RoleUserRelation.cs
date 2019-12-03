using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.RoleUserRelation
{
    public class RoleUserRelation : Entity
    {
        public string UserId { set; get; }

        public string RoleId { set; get; }
    }
}