using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.RoleInfo.Dto
{
    public class RoleInfoInputDto : InputDtoBase
    {
        public string rolename { set; get; }

        public string parentid { set; get; }
    }
}
