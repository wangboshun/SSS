using SSS.Domain.Seedwork.Model; 

namespace SSS.Domain.Permission.Relation.RoleRoleGroupRelation
{
    public class RoleRoleGroupRelation : Entity
    { 

        public string Roleid { set; get; }

        public string Rolegroupid { set; get; }

        public string Parentid { set; get; }
    }
}