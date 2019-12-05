using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.UserPowerGroupRelation.Dto
{
    public class UserPowerGroupRelationOutputDto : OutputDtoBase
    {
        public string powergroupid { set; get; }
        public string powergroupname { set; get; }
        public string userid { set; get; }
        public string username { set; get; }
    }
}
