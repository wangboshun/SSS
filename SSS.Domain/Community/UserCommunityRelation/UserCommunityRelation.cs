using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Community.UserCommunityRelation
{
    public class UserCommunityRelation : Entity
    {
        public string UserId { set; get; }

        public string CommunityId { set; get; }
    }
}