using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.UserGroupRelation
{
    public class UserGroupRelation : Entity
    {
        public string UserGroupId { set; get; }
        public string UserId { set; get; }
    }
}