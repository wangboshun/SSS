using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.PowerGroupRelation.Dto
{
    public class PowerGroupRelationOutputDto : OutputDtoBase
    {
        public string powergroupid { set; get; }
        public string powergroupname { set; get; }
        public string powerid { set; get; }
        public string powername { set; get; }
    }
}