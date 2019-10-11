using SSS.Domain.Seedwork.Model;
using System.ComponentModel.DataAnnotations;

namespace SSS.Domain.UserActivity.Dto
{
    public class UserActivityInputDto : InputDtoBase
    {
        [Required(ErrorMessage = "«Î ‰»Î≈≈–Ú")]
        public string activityid { set; get; }

        public string userid { set; get; }
        public string wechatname { set; get; }

        public int grouptotal { set; get; }

    }
}
