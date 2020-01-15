using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Community.CommunityBusinessRelation.Dto
{
    public class CommunityBusinessRelationInputDto : InputDtoBase
    {
        public string BusinessId { set; get; }
        public string CommunityId { set; get; }
    }
}