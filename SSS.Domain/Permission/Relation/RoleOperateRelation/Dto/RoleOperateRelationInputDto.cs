using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.RoleOperateRelation.Dto
{
    public class RoleOperateRelationInputDto : InputDtoBase
    {
        public string roleid { set; get; }
        public string operateid { set; get; }
    }
}
