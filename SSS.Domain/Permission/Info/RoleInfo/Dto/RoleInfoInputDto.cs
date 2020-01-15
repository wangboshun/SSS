using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Info.RoleInfo.Dto
{
    public class RoleInfoInputDto : InputDtoBase
    {
        public string parentid { set; get; }
        public string rolegroupid { set; get; }
        public string rolename { set; get; }
    }
}