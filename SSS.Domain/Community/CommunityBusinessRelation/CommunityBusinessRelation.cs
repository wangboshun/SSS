using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Community.CommunityBusinessRelation
{
    public class CommunityBusinessRelation : Entity
    {
        public string Businessid { set; get; }
        public string Communityid { set; get; }
    }
}