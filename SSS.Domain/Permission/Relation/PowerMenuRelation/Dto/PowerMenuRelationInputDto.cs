using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.PowerMenuRelation.Dto
{
    public class PowerMenuRelationInputDto : InputDtoBase
    {
        public string powerid { set; get; }
        public string menuid { set; get; }
    }
}
