using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Group.UserGroup.Dto
{
    public class UserGroupOutputDto : OutputDtoBase
    {
        public string userid { set; get; }
        public string groupid { set; get; }
        public string username { set; get; }
        public string groupname { set; get; }
    }
}
