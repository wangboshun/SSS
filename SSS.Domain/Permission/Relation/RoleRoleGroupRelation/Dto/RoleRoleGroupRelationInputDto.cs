using SSS.Domain.Seedwork.Model; 

namespace SSS.Domain.Permission.Relation.RoleRoleGroupRelation.Dto
{
    public class RoleRoleGroupRelationInputDto : InputDtoBase
    {
        public string roleid { set; get; }
        public string rolename { set; get; }
        public string rolegroupname { set; get; }
        public string rolegroupid { set; get; }
        public string parentid { set; get; }
    }
}
