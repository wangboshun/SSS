using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Group.RoleGroup.Dto
{
    public class RoleGroupInputDto : InputDtoBase
    {
        public string parentid { set; get; }
        public string powergroupid { set; get; }
        public string rolegroupname { set; get; }
    }
}