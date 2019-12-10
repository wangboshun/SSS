using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.RoleGroupPowerGroupRelation
{
    public class RoleGroupPowerGroupRelation : Entity
    {
        public string PowerGroupId { set; get; }

        public string RoleGroupId { set; get; }
    }
}