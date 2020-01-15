using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.UserGroupRelation.Dto
{
    public class UserGroupRelationOutputDto : OutputDtoBase
    {
        public string userid { set; get; }
        public string username { set; get; }
        public string usergroupname { set; get; }
        public string usergroupid { set; get; }
    }
}