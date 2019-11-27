using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.UserInfo.Dto
{
    public class UserInfoInputDto : InputDtoBase
    {
        public string username { set; get; }

        public string password { set; get; }
    }
}