using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.UserGroupRelation
{
    public class UserGroupRelation : Entity
    {
        public string UserId { set; get; }

        public string UserGroupId { set; get; }
    }
}