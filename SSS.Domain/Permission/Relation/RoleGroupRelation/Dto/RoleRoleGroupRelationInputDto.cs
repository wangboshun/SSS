using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.RoleGroupRelation.Dto
{
    public class RoleGroupRelationInputDto : InputDtoBase
    {
        public string parentid { set; get; }
        public string rolegroupid { set; get; }
        public string rolegroupname { set; get; }
        public string roleid { set; get; }
        public string rolename { set; get; }
    }
}