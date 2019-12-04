using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Group.UserGroup
{
    public class UserGroup : Entity
    {
        public string ParentId { set; get; }

        public string UserGroupName { set; get; }
    }
}