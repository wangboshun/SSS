using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Community.UserCommunityRelation
{
    public class UserCommunityRelation : Entity
    {
        public string CommunityId { set; get; }
        public string UserId { set; get; }
    }
}