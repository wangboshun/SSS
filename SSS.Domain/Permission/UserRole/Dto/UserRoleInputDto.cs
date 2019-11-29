using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.UserRole.Dto
{
    public class UserRoleInputDto : InputDtoBase
    {
        public string userid { set; get; }
        public string roleid { set; get; }
    }
}
