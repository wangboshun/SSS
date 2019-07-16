using SSS.Domain.Seedwork.Model; 

namespace SSS.Domain.UserApi.Dto
{
    public class UserApiInputDto : InputDtoBase
    { 
        public string ApiKey { set; get; }

        public string Secret { set; get; } 

        public string PassPhrase { set; get; }

        public string UserId { set; get; }
    }
}