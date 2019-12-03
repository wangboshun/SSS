using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.UserUserGroupRelation.Dto
{
    public class UserUserGroupRelationInputDto : InputDtoBase
    {
        public string userid { set; get; }
        public string username { set; get; }
        public string usergroupid { set; get; }
        public string usergroupname { set; get; }
        public string parentid { set; get; }
    }
}
 