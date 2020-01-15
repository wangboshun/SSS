using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Community.UserCommunityRelation.Dto
{
    public class UserCommunityRelationInputDto : InputDtoBase
    {
        public string communityid { set; get; }
        public string userid { set; get; }
    }
}