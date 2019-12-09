using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Group.UserGroup.Dto
{
    public class UserGroupInputDto : InputDtoBase
    {
        public string rolegroupid { set; get; }
        public string parentid { set; get; }
        public string usergroupname { set; get; }
    }
}
