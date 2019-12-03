using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.RoleUserRelation.Dto
{
    public class RoleUserRelationInputDto : InputDtoBase
    {
        public string userid { set; get; }
        public string roleid { set; get; }
    }
}
