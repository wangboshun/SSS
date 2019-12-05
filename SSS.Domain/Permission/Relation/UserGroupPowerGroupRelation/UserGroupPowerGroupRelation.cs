using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.UserGroupPowerGroupRelation
{
    public class UserGroupPowerGroupRelation : Entity
    {


        public string Usergroupid { set; get; }

        public string Powergroupid { set; get; }

        public string Parentid { set; get; }
    }
}