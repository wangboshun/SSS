using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.RoleMenuRelation.Dto
{
    public class RoleMenuRelationInputDto : InputDtoBase
    {
        public string roleid { set; get; }
        public string menuid { set; get; }
    }
}
