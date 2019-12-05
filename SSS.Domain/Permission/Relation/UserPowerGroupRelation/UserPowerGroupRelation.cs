using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Relation.UserPowerGroupRelation
{
    public class UserPowerGroupRelation : Entity
    {


        public string Userid { set; get; }

        public string Powergroupid { set; get; }

        public string Parentid { set; get; }
    }
}