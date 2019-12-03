using SSS.Domain.Seedwork.Model; 

namespace SSS.Domain.Permission.Relation.RoleUserGroupRelation
{
    public class RoleUserGroupRelation : Entity
    {  
        public string Roleid { set; get; }

        public string Usergroupid { set; get; }
    }
}