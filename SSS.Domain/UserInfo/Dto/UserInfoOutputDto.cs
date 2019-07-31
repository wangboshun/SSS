using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.UserInfo.Dto
{
    public class UserInfoOutputDto : OutputDtoBase
    {  
        public string openid { set; get; }

        public string name { set; get; } 
    }
}
