using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.PowerOperateRelation
{
    public class PowerOperateRelation : Entity
    {
        public string ParentId { set; get; }

        public string PowerId { set; get; }

        public string OperateId { set; get; }
    }
}