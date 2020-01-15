using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Community.CommunityBusinessRelation.Dto
{
    public class CommunityBusinessRelationOutputDto : OutputDtoBase
    {
        public string BusinessId { set; get; }
        public string CommunityId { set; get; }
    }
}