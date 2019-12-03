using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.RoleMenuRelation.Dto
{
    public class RoleMenuRelationOutputDto : OutputDtoBase
    {
        public string menuname { set; get; }
        public string menuid { set; get; }
        public string roleid { set; get; }
        public string rolename { set; get; }
    }
}
