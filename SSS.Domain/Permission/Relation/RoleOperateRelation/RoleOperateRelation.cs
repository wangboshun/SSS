using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.RoleOperateRelation
{
    public class RoleOperateRelation : Entity
    {
        public string OperateId { set; get; }

        public string RoleId { set; get; }
    }
}