using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.RoleGroupRelation.Dto
{
    public class RoleGroupRelationOutputDto : OutputDtoBase
    {
        public string roleid { set; get; }
        public string rolename { set; get; }
        public string rolegroupname { set; get; }
        public string rolegroupid { set; get; }
    }
}
