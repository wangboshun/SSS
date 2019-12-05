using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.UserGroupPowerGroupRelation.Dto
{
    public class UserGroupPowerGroupRelationInputDto : InputDtoBase
    {
        public string powergroupid { set; get; }
        public string powergroupname { set; get; }
        public string usergroupid { set; get; }
        public string usergroupname { set; get; }
        public string parentid { set; get; }
    }
}
