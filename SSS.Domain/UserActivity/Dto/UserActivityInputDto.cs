using SSS.Domain.Seedwork.Model; 

namespace SSS.Domain.UserActivity.Dto
{
    public class UserActivityInputDto : InputDtoBase
    { 
        public string activityid { set; get; }

        public string userid { set; get; }

        public string wechatname { set; get; }

        public int grouptotal { set; get; } 

    }
}
