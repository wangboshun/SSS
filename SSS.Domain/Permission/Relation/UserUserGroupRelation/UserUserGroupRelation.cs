using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.UserUserGroupRelation
{
    public class UserUserGroupRelation : Entity
    {
        public string UserId { set; get; }

        public string UserGroupId { set; get; }

    }
}