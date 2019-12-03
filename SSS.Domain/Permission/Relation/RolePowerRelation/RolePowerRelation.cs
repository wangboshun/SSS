using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.RolePowerRelation
{
    public class RolePowerRelation : Entity {


        public string ParentId { set; get; }

        public string PowerId { set; get; }

        public string RoleId { set; get; }
    }
}