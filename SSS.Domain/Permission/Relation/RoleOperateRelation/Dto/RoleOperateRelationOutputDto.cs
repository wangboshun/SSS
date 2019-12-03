using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.RoleOperateRelation.Dto
{
    public class RoleOperateRelationOutputDto : OutputDtoBase
    {
        public string operateid { set; get; }

        public string operatename { set; get; }
        public string roleid { set; get; }
        public string rolename { set; get; }

    }
}
