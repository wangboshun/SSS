using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Community.CommunityTask.Dto
{
    public class CommunityTaskInputDto : InputDtoBase
    {
        public string name { set; get; }
        public string detail { set; get; }
        public string userid { set; get; }
        public string email { set; get; }
        public string contact { set; get; }
        public string phone { set; get; }
        public string status { set; get; }
        public string businessid { set; get; }
    }
}
