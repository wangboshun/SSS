using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.UserRole
{
    public class UserRole : Entity
    {
        public string UserId { set; get; }

        public string RoleId { set; get; }
    }
}