using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.PowerGroupOperateRelation.Dto
{
    public class PowerGroupOperateRelationInputDto : InputDtoBase
    {
        public string powergroupid { set; get; }

        public string powergroupname { set; get; }

        public string operateid { set; get; }
        public string operatename { set; get; }

        public string parentid { set; get; }
    }
}