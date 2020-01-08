using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Community.CommunityBusinessRelation.Dto
{
    public class CommunityBusinessRelationInputDto : InputDtoBase
    {
        public string CommunityId { set; get; }
        public string BusinessId { set; get; }
    }
}
