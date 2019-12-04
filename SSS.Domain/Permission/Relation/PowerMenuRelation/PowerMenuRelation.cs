using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.PowerMenuRelation
{
    public class PowerMenuRelation : Entity
    {
        public string ParentId { set; get; }

        public string PowerId { set; get; }

        public string MenuId { set; get; }
    }
}