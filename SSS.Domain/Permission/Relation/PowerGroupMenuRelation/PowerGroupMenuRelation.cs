using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.PowerGroupMenuRelation
{
    public class PowerGroupMenuRelation : Entity
    {
        public string MenuId { set; get; }
        public string ParentId { set; get; }

        public string PowerGroupId { set; get; }
    }
}