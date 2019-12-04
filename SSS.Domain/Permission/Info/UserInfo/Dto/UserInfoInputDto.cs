using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Info.UserInfo.Dto
{
    public class UserInfoInputDto : InputDtoBase
    {
        public string username { set; get; }

        public string password { set; get; }

        public string usergroupid { set; get; }

        public string parentid { set; get; }
    }
}