using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.UserGroupPowerGroupRelation.Dto
{
    public class UserGroupPowerGroupRelationOutputDto : OutputDtoBase
    {
        public string powergroupid { set; get; }
        public string powergroupname { set; get; }
        public string usergroupid { set; get; }
        public string usergroupname { set; get; }
    }
}
