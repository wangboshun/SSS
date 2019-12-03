using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.RoleUserRelation.Dto
{
    public class RoleUserRelationOutputDto : OutputDtoBase
    {
        public string userid { set; get; }
        public string username { set; get; }
        public string roleid { set; get; }
        public string rolename { set; get; }
    }
}
