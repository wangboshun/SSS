using SSS.Domain.Seedwork.Model; 

namespace SSS.Domain.Community.CommunityTask.Dto
{
    public class CommunityTaskOutputDto : OutputDtoBase
    {
        public string Name { set; get; }
        public string Detail { set; get; }
        public string UserId { set; get; }
        public string Email { set; get; }
        public string Contact { set; get; }
        public string Phone { set; get; }
        public string Status { set; get; }
        public string BusinessId { set; get; }
    }
}
