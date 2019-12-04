using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.PowerGroupOperateRelation
{
    public class PowerGroupOperateRelation : Entity
    {
        public string ParentId { set; get; }

        public string PowerGroupId { set; get; }

        public string OperateId { set; get; }
    }
}