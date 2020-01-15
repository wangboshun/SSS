using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.UserGroupRoleGroupRelation
{
    public class UserGroupRoleGroupRelation : Entity
    {
        public string UserGroupId { set; get; }

        public string RoleGroupId { set; get; }
    }
}