using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.PowerGroupMenuRelation.Dto
{
    public class PowerGroupMenuRelationInputDto : InputDtoBase
    {
        public string powergroupid { set; get; }

        public string powergroupname { set; get; }

        public string menuid { set; get; }
        public string menuname { set; get; }

        public string parentid { set; get; }
    }
}