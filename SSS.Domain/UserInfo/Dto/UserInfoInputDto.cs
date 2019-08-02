using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.UserInfo.Dto
{
    public class UserInfoInputDto : InputDtoBase
    {
        public string phone { set; get; }

        public string openid { set; get; }
        public string code { set; get; }

        public string name { set; get; }

        public string encryptedData { set; get; }

        public string iv { set; get; }
    }
}
