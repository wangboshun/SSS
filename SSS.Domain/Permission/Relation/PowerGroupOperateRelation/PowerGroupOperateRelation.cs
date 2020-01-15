using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.PowerGroupOperateRelation
{
    public class PowerGroupOperateRelation : Entity
    {
        public string OperateId { set; get; }
        public string ParentId { set; get; }

        public string PowerGroupId { set; get; }
    }
}